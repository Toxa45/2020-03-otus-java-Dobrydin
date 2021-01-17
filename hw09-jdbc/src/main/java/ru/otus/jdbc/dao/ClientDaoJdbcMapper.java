package ru.otus.jdbc.dao;

import java.util.List;
import java.util.Optional;
import lombok.Getter;
import ru.otus.core.dao.Dao;
import ru.otus.core.model.Client;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.jdbc.mapper.JdbcMapper;

public class ClientDaoJdbcMapper implements Dao<Client,Long> {
  @Getter
  private final JdbcMapper<Client,Long> clientJdbcMapper;

  public ClientDaoJdbcMapper(
      JdbcMapper<Client,Long> clientJdbcMapper) {
    this.clientJdbcMapper = clientJdbcMapper;
  }

  @Override
  public Optional<Client> findById(Long id) {
    return clientJdbcMapper.findById(id);
  }

  @Override
  public Long insert(Client client) {
    return clientJdbcMapper.insert(client);
  }

  @Override
  public Long insertOrUpdate(Client client) {
    return clientJdbcMapper.insertOrUpdate(client);
  }

  @Override
  public SessionManager getSessionManager() {
    return clientJdbcMapper.getSessionManager();
  }


  @Override
  public List<Client> findAll() {
    return clientJdbcMapper.findAll();
  }
}
