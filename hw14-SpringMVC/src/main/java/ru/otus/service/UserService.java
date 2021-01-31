package ru.otus.service;

import java.util.Optional;
import ru.otus.model.User;

public interface UserService extends DBService<User,Long> {

  Optional<User> findByLogin(String login);
}
