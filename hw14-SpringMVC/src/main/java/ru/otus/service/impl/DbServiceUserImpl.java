package ru.otus.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.dao.UserDao;
import ru.otus.model.User;
import ru.otus.service.DbServiceException;
import ru.otus.service.UserService;

@Service
@RequiredArgsConstructor
public class DbServiceUserImpl implements UserService {

  private static final Logger logger = LoggerFactory.getLogger(DbServiceUserImpl.class);

  private final UserDao userDao;

  @Override
  public Long save(User user) {
    try (var sessionManager = userDao.getSessionManager()) {
      sessionManager.beginSession();
      try {
        var clientId = userDao.insertOrUpdate(user);
        sessionManager.commitSession();

        logger.info("created client: {}", clientId);
        return clientId;
      } catch (Exception e) {
        sessionManager.rollbackSession();
        throw new DbServiceException(e);
      }
    }
  }


  @Override
  public Optional<User> getById(Long id) {
    try (var sessionManager = userDao.getSessionManager()) {
      sessionManager.beginSession();
      try {
        Optional<User> userOptional = userDao.findById(id);

        logger.info("User: {}", userOptional.orElse(null));
        return userOptional;
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
      }
      return Optional.empty();
    }
  }

  @Override
  public List<User> findAll() {
    try (var sessionManager = userDao.getSessionManager()) {
      sessionManager.beginSession();
      try {
        List<User> users = userDao.findAll();

        logger.info("users {}", users);
        return users;
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
      }
      return Collections.emptyList();
    }
  }

  @Override
  public Optional<User> findByLogin(String login) {
    try (var sessionManager = userDao.getSessionManager()) {
      sessionManager.beginSession();
      try {
        Optional<User> user = userDao.findByLogin(login);

        logger.info("User: {}", user.orElse(null));
        return user;
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
      }
      return Optional.empty();
    }
  }
}
