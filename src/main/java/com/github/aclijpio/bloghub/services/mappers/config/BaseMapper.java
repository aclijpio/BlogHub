package com.github.aclijpio.bloghub.services.mappers.config;

import java.util.List;

public interface BaseMapper<E, T> {

    E toEntity(T dto);
    List<E> toEntityList(List<T> dtos);
    T toDto(E entity);
    List<T> toDtoList(List<E> entities);


}
