package ru.otus.jdbc.dao;

import java.util.List;
import java.util.Optional;
import lombok.Getter;
import ru.otus.core.dao.Dao;
import ru.otus.core.model.Account;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.jdbc.mapper.JdbcMapper;

public class AccountDaoJdbcMapper implements Dao<Account,String> {
  @Getter
  private final JdbcMapper<Account,String> accountJdbcMapper;

  public AccountDaoJdbcMapper(
      JdbcMapper<Account,String> accountJdbcMapper) {
    this.accountJdbcMapper = accountJdbcMapper;
  }

  @Override
  public Optional<Account> findById(String id) {
    return accountJdbcMapper.findById(id);
  }

  @Override
  public List<Account> findAll() {
    return accountJdbcMapper.findAll();
  }

  @Override
  public String insert(Account account) {
    return accountJdbcMapper.insert(account);
  }

  @Override
  public String insertOrUpdate(Account account) {
    return accountJdbcMapper.insertOrUpdate(account);
  }

  @Override
  public SessionManager getSessionManager() {
    return accountJdbcMapper.getSessionManager();
  }
}
