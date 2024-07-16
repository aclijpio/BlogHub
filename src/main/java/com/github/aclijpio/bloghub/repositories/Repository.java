package com.github.aclijpio.bloghub.repositories;

import java.io.Serializable;
import java.util.Optional;

public interface Repository<T, ID extends Serializable> {

    T save(T entity);
    Optional<T> findById(ID id);
    Iterable<T> findAll();
    void delete(T entity);
    void deleteById(ID id);

}
