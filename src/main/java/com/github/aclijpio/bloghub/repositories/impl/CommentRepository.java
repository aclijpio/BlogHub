package com.github.aclijpio.bloghub.repositories.impl;

import com.github.aclijpio.bloghub.entities.Comment;
import com.github.aclijpio.bloghub.exceptions.dababase.DatabaseOperationException;
import com.github.aclijpio.bloghub.exceptions.dababase.IdRetrievalException;
import com.github.aclijpio.bloghub.repositories.CrudRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommentRepository extends CrudRepository<Comment, Long> {

    public final static CommentRepository INSTANCE = new CommentRepository();

    private CommentRepository() {
    }

    @Override
    public Comment persist(Comment comment) {
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

    @Override
    public Comment merge(Comment comment) {
        return executeQuery("UPDATE comments SET content = ?", ps -> {
            try {
                ps.setString(1, comment.getContent());

                ps.execute();

                return comment;
            }
            catch (SQLException e) {
                throw new DatabaseOperationException("Failed to execute update query for comment with id: " + comment.getId(), e);
            }
        });
    }

    @Override
    public Optional<Comment> findById(Long aLong) {
        return executeQuery("SELECT * FROM comments WHERE id = ?", ps -> {
            try {
                ps.setLong(1, aLong);
                ResultSet resultSet = ps.executeQuery();

                if (resultSet.next()) {
                    Comment comment =  new Comment();

                    comment.setId(resultSet.getLong("id"));
                    comment.setContent(resultSet.getString("content"));

                    return Optional.of(comment);
                }

                return Optional.empty();

            } catch (SQLException e) {
                throw new DatabaseOperationException("Failed to execute select query for comment with id: " + aLong, e);
            }
        });
    }

    @Override
    public Iterable<Comment> findAll() {
        return executeQuery("SELECT * FROM users", ps -> {
            try {
                ResultSet resultSet = ps.executeQuery();
                List<Comment> comments = new ArrayList<>();

                while (resultSet.next()) {
                    Comment comment =  new Comment();

                    comment.setId(resultSet.getLong("id"));
                    comment.setContent(resultSet.getString("content"));

                    comments.add(comment);
                }

                return comments;

            } catch (SQLException e) {
                throw new DatabaseOperationException("Failed to execute select query for comments", e);
            }
        });
    }

    @Override
    public boolean delete(Comment comment) {
        deleteById(comment.getId());
        return false;
    }

    @Override
    public boolean deleteById(Long aLong) {
        executeQuery("DELETE FROM comments WHERE id = ?", ps -> {
            try {
                ps.setLong(1, aLong);
                ps.execute();
            }
            catch (SQLException e) {
                throw new DatabaseOperationException("Failed to execute delete query for comments with id: " + aLong, e);
            }
        });
        return false;
    }

    @Override
    public boolean checkIdExists(Comment comment) {
        if (comment.getId() == null) return false;
        return executeQuery("SElECT EXISTS (SELECT id FROM comments WHERE id = ?)", ps -> {
            try {
                ps.setLong(1, comment.getId());
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next())
                    return resultSet.getBoolean(1);

                return false;


            } catch (SQLException e) {
                throw new DatabaseOperationException("Failed to execute check query for comment with id: " + comment.getId(), e);
            }
        });
    }
}
