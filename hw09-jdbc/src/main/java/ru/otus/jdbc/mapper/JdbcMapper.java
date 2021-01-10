package ru.otus.jdbc.mapper;

import java.util.List;
import java.util.Optional;
import ru.otus.core.sessionmanager.SessionManager;

/**
 * Сохратяет объект в базу, читает объект из базы
 * @param <T>
 */
public interface JdbcMapper<T,ID> {
    ID insert(T objectData);

    ID insertOrUpdate(T objectData);

    Optional<T> findById(ID id);
    List<T> findAll();
    SessionManager getSessionManager();
}
