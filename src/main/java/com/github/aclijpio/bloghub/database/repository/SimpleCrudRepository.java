package com.github.aclijpio.bloghub.database.repository;

import com.github.aclijpio.bloghub.database.field.entity.EntityInfo;
import com.github.aclijpio.bloghub.database.util.EntityAnnotationUtil;
import com.github.aclijpio.bloghub.database.util.DefaultConnectionPool;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public abstract class SimpleCrudRepository<T, ID> implements CrudRepository<T, ID> {


    private final EntityInfo entityInfo;

    protected SimpleCrudRepository() {
        this.entityInfo = EntityAnnotationUtil.get(this.getInnerClassType());
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
        return executeQuery("select * from %s".formatted(entityInfo.getTableName()), ps -> {

            try (ResultSet rs = ps.executeQuery()) {
                List<T> results = new ArrayList<>();
                while (rs.next()) {
                    System.out.println(rs.getString(1));
                }
                return results;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        });
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

    public <R> R executeQuery(String query, Function<PreparedStatement, R> function) {
        try (Connection connection = DefaultConnectionPool.UTIL.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            return function.apply(ps);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @SuppressWarnings("unchecked")
    protected Class<T> getInnerClassType(){
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        return (Class<T>) pt.getActualTypeArguments()[0];
    }
}

