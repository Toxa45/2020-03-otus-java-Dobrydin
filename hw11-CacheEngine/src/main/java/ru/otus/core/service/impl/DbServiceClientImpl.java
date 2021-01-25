package ru.otus.core.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.core.dao.Dao;
import ru.otus.core.model.Client;
import ru.otus.core.service.DBService;
import ru.otus.core.service.DbServiceException;

public class DbServiceClientImpl implements DBService<Client, Long> {

  private static final Logger logger = LoggerFactory.getLogger(DbServiceClientImpl.class);

  private final Dao<Client, Long> clientDao;
  private final HwCache<Long, Client> cache;

  public DbServiceClientImpl(Dao<Client, Long> clientDao, HwCache<Long, Client> cache) {
    this.clientDao = clientDao;
    this.cache = cache;
  }

  @Override
  public Long save(Client client) {
    try (var sessionManager = clientDao.getSessionManager()) {
      sessionManager.beginSession();
      try {
        var clientId = clientDao.insertOrUpdate(client);
        sessionManager.commitSession();

        putCache(clientId, client);
        logger.info("created client: {}", clientId);
        return clientId;
      } catch (Exception e) {
        sessionManager.rollbackSession();
        throw new DbServiceException(e);
      }
    }
  }
  @Override
  public Optional<Client> getById(Long id) {
    try (var sessionManager = clientDao.getSessionManager()) {
      sessionManager.beginSession();
      try {
        Client cacheClient = getCacheClient(id);
        if(cacheClient!=null) {
          logger.info("get cache: {}", cacheClient);
          return Optional.of(cacheClient);
        }
        Optional<Client> clientOptional = clientDao.findById(id);

        clientOptional.ifPresent(client -> putCache(id, client));

        logger.info("client: {}", clientOptional.orElse(null));
        return clientOptional;
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
      }
      return Optional.empty();
    }
  }

  @Override
  public List<Client> findAll() {
    try (var sessionManager = clientDao.getSessionManager()) {
      sessionManager.beginSession();
      try {
        List<Client> clients = clientDao.findAll();

        logger.info("clients: {}", clients);
        return clients;
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
      }
      return Collections.EMPTY_LIST;
    }
  }


  private Client getCacheClient(Long id){
    if(cache==null)
      return null;
    return cache.get(id);
  }


  private void putCache(Long id, Client client){
    if(cache!=null)
      cache.put(id, client);
  }

}
