package ru.otus.hibernate.dao;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.Dao;
import ru.otus.core.model.Account;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.hibernate.exception.DaoException;
import ru.otus.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

public class AccountHibernate implements Dao<Account, String> {

  private static final Logger logger = LoggerFactory.getLogger(AccountHibernate.class);

  private final SessionManagerHibernate sessionManager;

  public AccountHibernate(SessionManagerHibernate sessionManager) {
    this.sessionManager = sessionManager;
  }

  @Override
  public Optional<Account> findById(String id) {
    DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
    try {
      return Optional.ofNullable(currentSession.getHibernateSession().find(Account.class, id));
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    return Optional.empty();
  }

  @Override
  public List<Account> findAll() {
    DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
    try {
      return currentSession.getHibernateSession().createNamedQuery("account.findAll", Account.class)
          .getResultList();
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    return Collections.EMPTY_LIST;
  }

  @Override
  public String insert(Account account) {
    DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
    try {
      Session hibernateSession = currentSession.getHibernateSession();
      hibernateSession.persist(account);
      hibernateSession.flush();
      return account.getNo();
    } catch (Exception e) {
      throw new DaoException(e);
    }
  }

  private void update(Account account) {
    DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
    try {
      Session hibernateSession = currentSession.getHibernateSession();
      hibernateSession.merge(account);
    } catch (Exception e) {
      throw new DaoException(e);
    }
  }

  @Override
  public String insertOrUpdate(Account account) {
    DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
    try {
      Session hibernateSession = currentSession.getHibernateSession();
      hibernateSession.saveOrUpdate(account);
      hibernateSession.flush();
      return account.getNo();
    } catch (Exception e) {
      throw new DaoException(e);
    }
  }


  @Override
  public SessionManager getSessionManager() {
    return sessionManager;
  }
}
