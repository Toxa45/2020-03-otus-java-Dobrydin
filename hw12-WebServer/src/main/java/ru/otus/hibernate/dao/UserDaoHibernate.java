package ru.otus.hibernate.dao;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.dao.UserDao;
import ru.otus.hibernate.exception.DaoException;
import ru.otus.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.model.User;
import ru.otus.sessionmanager.SessionManager;

public class UserDaoHibernate implements UserDao {

  private static final Logger logger = LoggerFactory.getLogger(UserDaoHibernate.class);

  private final SessionManagerHibernate sessionManager;

  public UserDaoHibernate(SessionManagerHibernate sessionManager) {
    this.sessionManager = sessionManager;
  }

  @Override
  public Optional<User> findById(Long id) {
    DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
    try {
      return Optional.ofNullable(currentSession.getHibernateSession().find(User.class, id));
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    return Optional.empty();
  }

  @Override
  public List<User> findAll() {
    DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
    try {
      return currentSession.getHibernateSession().createNamedQuery("user.findAll", User.class)
          .getResultList();
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    return Collections.emptyList();
  }


  @Override
  public Long insertOrUpdate(User user) {
    DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
    try {
      Session hibernateSession = currentSession.getHibernateSession();
      if (user.getId() > 0) {
        hibernateSession.merge(user);
      } else {
        hibernateSession.persist(user);
        hibernateSession.flush();
      }
      return user.getId();
    } catch (Exception e) {
      throw new DaoException(e);
    }
  }


  @Override
  public SessionManager getSessionManager() {
    return sessionManager;
  }

  @Override
  public Optional<User> findByLogin(String login) {
    DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
    try {
      List<User> users = currentSession.getHibernateSession()
          .createQuery("select u from User u where u.login = :login", User.class)
          .setParameter("login", login)
          .getResultList();

      if (users.isEmpty()) {
        return Optional.empty();
      } else {
        return  Optional.of(users.get(0));
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    return Optional.empty();
  }
}
