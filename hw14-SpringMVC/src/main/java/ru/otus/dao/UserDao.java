package ru.otus.dao;

import java.util.Optional;
import ru.otus.model.User;

public interface UserDao extends Dao<User, Long> {

  Optional<User> findByLogin(String login);
}
