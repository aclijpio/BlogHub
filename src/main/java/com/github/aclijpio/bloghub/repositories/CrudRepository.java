package com.github.aclijpio.bloghub.repositories;

import com.github.aclijpio.bloghub.util.DefaultConnectionPool;
import lombok.extern.slf4j.Slf4j;
import org.intellij.lang.annotations.Language;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;


@Slf4j
public abstract class CrudRepository<T, ID> {

    void save(T t){
        if (checkIdExists(t))
            this.merge(t);
        else
            this.persist(t);
    }

    abstract void persist(T t);
    abstract void merge(T t);

    abstract Iterable<T> saveAll(Iterable<T> iterable);
    abstract Optional<T> findById(ID id);
    abstract Iterable<T> findAll();
    abstract void delete(T t);
    abstract void deleteById(ID id);

    abstract boolean checkIdExists(T t);


    public <R> R executeQuery(@Language("SQL") String query, Function<PreparedStatement, R> function) {
        try (Connection connection = DefaultConnectionPool.UTIL.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            return function.apply(ps);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void executeQuery(@Language("SQL") String query, Consumer<PreparedStatement> function) {
        try (Connection connection = DefaultConnectionPool.UTIL.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            function.accept(ps);
        } catch (SQLException e) {
            log.error("Failed to execute query: %s".formatted(query), e);
        }
    }

}
