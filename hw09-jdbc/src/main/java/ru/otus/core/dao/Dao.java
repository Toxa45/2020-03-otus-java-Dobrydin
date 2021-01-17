package ru.otus.core.dao;

import java.util.List;
import java.util.Optional;
import ru.otus.core.sessionmanager.SessionManager;

public interface Dao<T, ID> {
    Optional<T> findById(ID id);
    List<T> findAll();

    ID insert(T entity);

    ID insertOrUpdate(T entity);

    SessionManager getSessionManager();
}
