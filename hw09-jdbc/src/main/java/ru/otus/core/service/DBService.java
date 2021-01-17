package ru.otus.core.service;

import java.util.List;
import java.util.Optional;

public interface DBService<T,ID> {

    ID save(T entity);

    Optional<T> getById(ID id);

    List<T> findAll();
}
