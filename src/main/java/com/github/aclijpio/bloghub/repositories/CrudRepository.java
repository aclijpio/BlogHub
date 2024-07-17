package com.github.aclijpio.bloghub.repositories;

import com.github.aclijpio.bloghub.utils.DefaultConnectionPool;
import lombok.extern.slf4j.Slf4j;
import org.intellij.lang.annotations.Language;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;


@Slf4j
public abstract class CrudRepository<T, ID extends Serializable> implements Repository<T, ID>{

    public T save(T t){
        if (checkIdExists(t))
            return this.merge(t);
        else
            return this.persist(t);
    }

    public abstract T persist(T t);
    public abstract T merge(T t);

    public abstract Optional<T> findById(ID id);
    public abstract Iterable<T> findAll();
    public abstract boolean delete(T t);
    public abstract boolean deleteById(ID id);

    public abstract boolean checkIdExists(T t);


    public <R> R executeQuery(@Language("SQL") String query, Function<PreparedStatement, R> function) {
        try (Connection connection = DefaultConnectionPool.UTIL.getConnection();
             PreparedStatement ps = connection.prepareStatement(query,  Statement.RETURN_GENERATED_KEYS)) {
            return function.apply(ps);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void executeQuery(@Language("SQL") String query, Consumer<PreparedStatement> function) {
        try (Connection connection = DefaultConnectionPool.UTIL.getConnection();
             PreparedStatement ps = connection.prepareStatement(query,  Statement.RETURN_GENERATED_KEYS)) {
            function.accept(ps);
        } catch (SQLException e) {
            log.error("Failed to execute query: %s".formatted(query), e);
        }
    }

}
