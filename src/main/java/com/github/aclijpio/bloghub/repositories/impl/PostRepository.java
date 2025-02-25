package com.github.aclijpio.bloghub.repositories.impl;

import com.github.aclijpio.bloghub.entities.Comment;
import com.github.aclijpio.bloghub.entities.Post;
import com.github.aclijpio.bloghub.entities.User;
import com.github.aclijpio.bloghub.exceptions.dababase.DatabaseOperationException;
import com.github.aclijpio.bloghub.exceptions.dababase.FailedToConvertException;
import com.github.aclijpio.bloghub.exceptions.dababase.IdRetrievalException;
import com.github.aclijpio.bloghub.exceptions.entity.UserNotFoundException;
import com.github.aclijpio.bloghub.repositories.CrudRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostRepository extends CrudRepository<Post, Long> {

    public final static PostRepository INSTANCE = new PostRepository();

    private final static  UserRepository userRepository = UserRepository.INSTANCE;
    private final static CommentRepository commentRepository = CommentRepository.INSTANCE;


    private PostRepository() {
    }

    @Override
    public Post persist(Post post) {
        return executeQuery("INSERT INTO posts (user_id, title, content, createdate, updatedate) VALUES (?, ?, ?, ?, ?)", ps -> {
            try {
                User author = post.getUser();
                if (author == null)
                    ps.setNull(1, Types.BIGINT);
                else
                    ps.setLong(1, author.getId());

                ps.setString(2, post.getTitle());
                ps.setString(3, post.getContent());
                ps.setTimestamp(4, Timestamp.valueOf(post.getCreatedDate()));
                ps.setTimestamp(5, Timestamp.valueOf(post.getUpdatedDate()));


                ps.executeUpdate();


                try(ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next())
                        post.setId(generatedKeys.getLong(1));
                    else
                        throw new IdRetrievalException("Failed to retrieve id for post");
                }
                post.setComments(findCommentsByPostId(post.getId()));
                return post;
            }
            catch (SQLException e) {
                throw new DatabaseOperationException("Failed to execute insert query for post "  + e);
            }
        });
    }

    @Override
    public Post merge(Post post) {
        return executeQuery("UPDATE posts SET title = ?, content = ?, updatedate = ? WHERE id = ?", ps -> {
            try {
                ps.setString(1, post.getTitle());
                ps.setString(2, post.getContent());
                ps.setTimestamp(3,Timestamp.valueOf(post.getUpdatedDate()));
                ps.setLong(4, post.getId());

                ps.execute();
                return post;
            }
            catch (SQLException e) {
                throw new DatabaseOperationException("Failed to execute update query for post with id: " + post.getId(), e);
            }
        });
    }


    @Override
    public Optional<Post> findById(Long aLong) {
        return executeQuery("SELECT * FROM posts WHERE id = ?", ps -> {
            try {
                ps.setLong(1, aLong);
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    Post post = mapPost(resultSet);
                    post.setComments(findCommentsByPostId(post.getId()));
                    return Optional.of(post);
                }
                return Optional.empty();

            } catch (SQLException e) {
                throw new DatabaseOperationException("Failed to execute select query for user with id: " + aLong, e);
            }
        });
    }

    @Override
    public List<Post> findAll() {
        return executeQuery("SELECT * FROM posts", ps -> {
            try {

                ResultSet resultSet = ps.executeQuery();

                List<Post> posts = new ArrayList<>();

                int i = 0;

                while (resultSet.next()) {

                    Post post = mapPost(resultSet);
                    post.setComments(findCommentsByPostId(post.getId()));

                    posts.add(post);
                }

                return posts;

            } catch (SQLException e) {
                throw new DatabaseOperationException("Failed to execute select query for posts", e);
            }
        });
    }

    public List<Post> findAllByDate(LocalDateTime date) {
        return executeQuery("SELECT * FROM posts WHERE createdate >= ?", ps -> {
            try {
                ps.setTimestamp(1, Timestamp.valueOf(date));

                ResultSet resultSet = ps.executeQuery();
                List<Post> posts = new ArrayList<>();

                while (resultSet.next()) {
                    Post post = mapPost(resultSet);

                    post.setComments(findCommentsByPostId(post.getId()));
                    posts.add(post);
                }

                return posts;

            } catch (SQLException e) {
                throw new DatabaseOperationException("Failed to execute select query for posts from date: " + date, e);
            }
        });
    }


    @Override
    public boolean delete(Post post) {
        return deleteById(post.getId());
    }

    @Override
    public boolean deleteById(Long aLong) {
        executeQuery("DELETE FROM posts WHERE id = ?", ps -> {
            try {
                ps.setLong(1, aLong);
                ps.execute();
            }
            catch (SQLException e) {
                throw new DatabaseOperationException("Failed to execute delete query for user with id: " + aLong, e);
            }
        });
        return true;
    }


    @Override
    public boolean checkIdExists(Post post) {
        if (post.getId() == null) return false;
        return executeQuery("SElECT EXISTS (SELECT id FROM posts WHERE id = ?)", ps -> {
            try {
                ps.setLong(1, post.getId());
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getBoolean(1);
                }
                return false;

            } catch (SQLException e) {
                throw new DatabaseOperationException("Failed to execute check query for post with id: " + post.getId(), e);
            }
        });
    }
    public void savePostCommentLink(Long postId, Comment comment) {

        saveComment(comment);

        executeQuery("INSERT INTO postscomments (post_id, comment_id) VALUES (?, ?)", ps -> {
            try {
                ps.setLong(1, postId);
                ps.setLong(2, comment.getId());
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new DatabaseOperationException("Failed to execute insert query for postcomment link " + e);
            }
        });
    }
    private Comment saveComment(Comment comment){
        return executeQuery("INSERT INTO comments (content) VALUES (?)", ps -> {
            try {
                ps.setString(1, comment.getContent());

                ps.executeUpdate();
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next())
                        comment.setId(generatedKeys.getLong(1));
                    else
                        throw new IdRetrievalException("Failed to retrieve id for comment");
                }

                return comment;
            }
            catch (SQLException e) {
                throw new DatabaseOperationException("Failed to execute insert query for comment", e);
            }
        });
    }
    private List<Comment> findCommentsByPostId(Long postId) {
        return executeQuery("SELECT * FROM postscomments WHERE post_id = ?", ps -> {
            List<Comment> comments = new ArrayList<>();
            try {
                ps.setLong(1, postId);
                ResultSet resultSet = ps.executeQuery();

                while (resultSet.next()) {

                    Optional<Comment> optionalComment = commentRepository.findById(resultSet.getLong("comment_id"));

                    optionalComment.ifPresent(comments::add);

                }
            } catch (SQLException e) {
                throw new DatabaseOperationException("Failed to execute select query for comments with post_id: " + postId, e);
            }
            return comments;
        });
    }

    private static Post mapPost(ResultSet resultSet) throws SQLException {
        try {
            Post post = new Post();
            post.setId(resultSet.getLong("id"));

            Long userId = resultSet.getLong("user_id");
            Optional<User> optionalUser = userRepository.findById(userId);

            post.setUser(optionalUser.orElseThrow(() ->
                    new UserNotFoundException("User not found with id " + userId)));

            post.setTitle(resultSet.getString("title"));
            post.setContent(resultSet.getString("content"));
            post.setCreatedDate(resultSet.getTimestamp("createDate").toLocalDateTime());
            post.setUpdatedDate(resultSet.getTimestamp("updateDate").toLocalDateTime());


            return post;
        } catch (SQLException e){
            throw new FailedToConvertException("Failed to convert post. " + e.getMessage());
        }

    }
}
