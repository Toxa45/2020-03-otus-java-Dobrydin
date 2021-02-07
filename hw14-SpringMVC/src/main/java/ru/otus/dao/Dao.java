package ru.otus.dao;

import java.util.List;
import java.util.Optional;
import ru.otus.sessionmanager.SessionManager;

public interface Dao<T, ID> {

  Optional<T> findById(ID id);

  List<T> findAll();

  ID insertOrUpdate(T entity);

  SessionManager getSessionManager();
}
