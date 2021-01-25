package ru.otus.core.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.Dao;
import ru.otus.core.model.Account;
import ru.otus.core.service.DBService;
import ru.otus.core.service.DbServiceException;

public class DbServiceAccountImpl implements DBService<Account, String> {

  private static final Logger logger = LoggerFactory.getLogger(DbServiceAccountImpl.class);

  private final Dao<Account, String> accountDao;

  public DbServiceAccountImpl(Dao<Account, String> accountDao) {
    this.accountDao = accountDao;
  }

  @Override
  public String save(Account account) {
    try (var sessionManager = accountDao.getSessionManager()) {
      sessionManager.beginSession();
      try {
        var accountId = accountDao.insertOrUpdate(account);
        sessionManager.commitSession();

        logger.info("created account: {}", accountId);
        return accountId;
      } catch (Exception e) {
        sessionManager.rollbackSession();
        throw new DbServiceException(e);
      }
    }
  }

  @Override
  public Optional<Account> getById(String id) {
    try (var sessionManager = accountDao.getSessionManager()) {
      sessionManager.beginSession();
      try {
        Optional<Account> accountOptional = accountDao.findById(id);

        logger.info("account: {}", accountOptional.orElse(null));
        return accountOptional;
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
      }
      return Optional.empty();
    }
  }

  @Override
  public List<Account> findAll() {
    try (var sessionManager = accountDao.getSessionManager()) {
      sessionManager.beginSession();
      try {
        List<Account> accounts = accountDao.findAll();

        logger.info("accounts: {}", accounts);
        return accounts;
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
        sessionManager.rollbackSession();
      }
      return Collections.EMPTY_LIST;
    }
  }

}
