package com.github.aclijpio.bloghub.repositories;

import com.github.aclijpio.bloghub.entities.User;
import com.github.aclijpio.bloghub.exceptions.DatabaseOperationException;
import com.github.aclijpio.bloghub.exceptions.IdRetrievalException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository extends CrudRepository<User, Long> {

    @Override
    public User persist(User user) {
        return executeQuery("INSERT INTO users (username, email) VALUES (?, ?)", ps -> {
            try {
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getEmail());

                ps.executeUpdate();
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next())
                        user.setId(generatedKeys.getLong(1));
                    else
                        throw new IdRetrievalException("Failed to retrieve id for user with username: %s \n".formatted(user.getUsername()));
                }

                return user;
            }
            catch (SQLException e) {
                throw new DatabaseOperationException("Failed to execute insert query for user with id: " + user.getId(), e);
            }
        });
    }

    @Override
    public User merge(User user) {
        return executeQuery("UPDATE users SET username = ?, email = ? WHERE id = ?", ps -> {
            try {
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getEmail());
                ps.setLong(3, user.getId());

                ps.execute();

                return user;
            }
            catch (SQLException e) {
                throw new DatabaseOperationException("Failed to execute update query for user with id: " + user.getId(), e);
            }
        });
    }

    @Override
    public Optional<User> findById(Long aLong) {

        return executeQuery("SELECT * FROM users WHERE id = ?", ps -> {
            try {
                ps.setLong(1, aLong);
                ResultSet resultSet = ps.executeQuery();

                if (resultSet.next()) {
                    User user =  new User();

                    user.setId(resultSet.getLong("id"));
                    user.setUsername(resultSet.getString("username"));
                    user.setEmail(resultSet.getString("email"));

                    return Optional.of(user);
                }

                return Optional.empty();

            } catch (SQLException e) {
                throw new DatabaseOperationException("Failed to execute select query for user with id: " + aLong, e);
            }
        });
    }

    @Override
    public Iterable<User> findAll() {
        return executeQuery("SELECT * FROM users", ps -> {
            try {
                ResultSet resultSet = ps.executeQuery();
                List<User> users = new ArrayList<>();

                while (resultSet.next()) {
                    User user =  new User();

                    user.setId(resultSet.getLong("id"));
                    user.setUsername(resultSet.getString("username"));
                    user.setEmail(resultSet.getString("email"));

                    users.add(user);
                }

                return users;

            } catch (SQLException e) {
                throw new DatabaseOperationException("Failed to execute select query for users", e);
            }
        });
    }



    @Override
    public void delete(User user) {
        deleteById(user.getId());
    }

    @Override
    public void deleteById(Long aLong) {
        executeQuery("DELETE FROM users WHERE id = ?", ps -> {
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
    boolean checkIdExists(User user) {
        if (user.getId() == null) return false;
        return executeQuery("SElECT EXISTS (SELECT id FROM users WHERE id = ?)", ps -> {
            try {
                ps.setLong(1, user.getId());
                ResultSet resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getBoolean(1);
                }
                return false;

            } catch (SQLException e) {
                throw new DatabaseOperationException("Failed to execute check query for user with id: " + user.getId(), e);
            }
        });
    }
    private static User mapUser(ResultSet rs) throws SQLException {
        User user =  new User();

        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));

        return user;
    }
}
