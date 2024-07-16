package com.github.aclijpio.bloghub.repositories;

import com.github.aclijpio.bloghub.entities.Post;
import com.github.aclijpio.bloghub.entities.User;
import com.github.aclijpio.bloghub.exceptions.DatabaseOperationException;
import com.github.aclijpio.bloghub.exceptions.IdRetrievalException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class PostRepository extends CrudRepository<Post, Long> {
    @Override
    public Post persist(Post post) {
        return executeQuery("INSERT INTO posts (user_id, title, content, createdate, updatedate) VALUES (?, ?, ?, ?, ?)", ps -> {
            try {

                User author = post.getUser();
                if (author == null)
                    ps.setNull(1, Types.BIGINT);
                else
                    ps.setLong(1, post.getUser().getId());
                ps.setString(2, post.getTitle());
                ps.setString(3, post.getContent());
                ps.setTimestamp(4, Timestamp.valueOf(post.getCreatedDate()));
                ps.setTimestamp(5, Timestamp.valueOf(post.getUpdatedDate()));


                ps.executeUpdate();
                //Comments


                try(ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next())
                        post.setId(generatedKeys.getLong(1));
                    else
                        throw new IdRetrievalException("Failed to retrieve id for post");
                }
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
        return executeQuery("SELECT * FROM users WHERE id = ?", ps -> {
            try {
                ps.setLong(1, aLong);
                ResultSet resultSet = ps.executeQuery();

                if (resultSet.next()) {

                    return Optional.of(mapPost(resultSet));
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

                while (resultSet.next()) {
                    posts.add(mapPost(resultSet));
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
                    posts.add(mapPost(resultSet));
                }

                return posts;

            } catch (SQLException e) {
                throw new DatabaseOperationException("Failed to execute select query for posts from date: " + date, e);
            }
        });
    }


    @Override
    public void delete(Post post) {
        deleteById(post.getId());
    }

    @Override
    public void deleteById(Long aLong) {
        executeQuery("DELETE FROM posts WHERE id = ?", ps -> {
            try {
                ps.setLong(1, aLong);
                ps.execute();
            }
            catch (SQLException e) {
                throw new DatabaseOperationException("Failed to execute delete query for user with id: " + aLong, e);
            }
        });
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

    private static Post mapPost(ResultSet resultSet) throws SQLException {
        Post post =  new Post();

        post.setId(resultSet.getLong("id"));
        post.setTitle(resultSet.getString("title"));
        post.setContent(resultSet.getString("content"));
        post.setCreatedDate(resultSet.getTimestamp("createDate").toLocalDateTime());
        post.setUpdatedDate(resultSet.getTimestamp("updateDate").toLocalDateTime());

        return post;


    }
}
