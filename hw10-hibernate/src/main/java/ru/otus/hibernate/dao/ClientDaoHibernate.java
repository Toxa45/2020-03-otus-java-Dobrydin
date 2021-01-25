package ru.otus.hibernate.dao;


import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.Dao;
import ru.otus.core.model.Client;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.hibernate.exception.DaoException;
import ru.otus.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

public class ClientDaoHibernate implements Dao<Client, Long> {

  private static final Logger logger = LoggerFactory.getLogger(ClientDaoHibernate.class);

  private final SessionManagerHibernate sessionManager;

  public ClientDaoHibernate(SessionManagerHibernate sessionManager) {
    this.sessionManager = sessionManager;
  }


  @Override
  public Optional<Client> findById(Long id) {
    DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
    try {
      return Optional.ofNullable(currentSession.getHibernateSession().find(Client.class, id));
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    return Optional.empty();
  }

  @Override
  public List<Client> findAll() {
    DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
    try {
      return currentSession.getHibernateSession().createNamedQuery("client.findAll", Client.class)
          .getResultList();
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    return Collections.EMPTY_LIST;
  }

  @Override
  public Long insert(Client client) {
    DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
    try {
      Session hibernateSession = currentSession.getHibernateSession();
      hibernateSession.persist(client);
      hibernateSession.flush();
      return client.getId();
    } catch (Exception e) {
      throw new DaoException(e);
    }
  }

  private void update(Client client) {
    DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
    try {
      Session hibernateSession = currentSession.getHibernateSession();
      hibernateSession.merge(client);
    } catch (Exception e) {
      throw new DaoException(e);
    }
  }

  @Override
  public Long insertOrUpdate(Client client) {
    DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
    try {
      Session hibernateSession = currentSession.getHibernateSession();
      if (client.getId() > 0) {
        hibernateSession.merge(client);
      } else {
        hibernateSession.persist(client);
        hibernateSession.flush();
      }
      return client.getId();
    } catch (Exception e) {
      throw new DaoException(e);
    }
  }


  @Override
  public SessionManager getSessionManager() {
    return sessionManager;
  }
}
