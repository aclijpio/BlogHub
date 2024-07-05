package com.github.aclijpio.bloghub.repositories;

import java.util.Optional;

public interface CrudRepository<T, ID> {

    T save(T t);
    Iterable<T> saveAll(Iterable<T> iterable);
    Optional<T> findById(ID id);
    Iterable<T> findAll();
    Iterable<T> findAllById(Iterable<ID> iterable);
    void delete(T t);
    void deleteById(ID id);
    void deleteAll();

}
