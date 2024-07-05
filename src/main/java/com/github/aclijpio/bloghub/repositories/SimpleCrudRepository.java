package com.github.aclijpio.bloghub.repositories;

import com.github.aclijpio.bloghub.utils.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Function;

public abstract class SimpleCrudRepository<T, ID> implements CrudRepository<T, ID>{


    private final Class<T> clazz;

    protected SimpleCrudRepository(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T save(T t) {
        return null;
    }

    @Override
    public Iterable<T> saveAll(Iterable<T> iterable) {
        return null;
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.empty();
    }

    @Override
    public Iterable<T> findAll() {
        return null;
    }

    @Override
    public Iterable<T> findAllById(Iterable<ID> iterable) {
        return null;
    }

    @Override
    public void delete(T t) {

    }

    @Override
    public void deleteById(ID id) {

    }

    @Override
    public void deleteAll() {

    }

    private <R> R executeInsideConnection(String query, Function<PreparedStatement, R> function){
        try(Connection connection = ConnectionPool.UTIL.getConnection();
            PreparedStatement ps = connection.prepareStatement(query))
        {
            return function.apply(ps);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
