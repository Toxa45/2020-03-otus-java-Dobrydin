package ru.otus.core.service.impl;


import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.core.service.impl.FlywayMigrations.flywayMigrations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.config.HWDataSource;
import ru.otus.core.dao.Dao;
import ru.otus.core.model.Client;
import ru.otus.core.service.DBService;
import ru.otus.jdbc.DbExecutorImpl;
import ru.otus.jdbc.dao.ClientDaoJdbcMapper;
import ru.otus.jdbc.mapper.JdbcMapper;
import ru.otus.jdbc.mapper.impl.JdbcMapperImpl;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

@DisplayName("Сервис для сохранения/обновления клиентов в БД")
class DbServiceClientImplTest {
  DBService<Client, Long> serviceClient;

  @BeforeEach
  void init() {
    var dataSource = new HWDataSource();
    flywayMigrations(dataSource);
    var sessionManager = new SessionManagerJdbc(dataSource);
    DbExecutorImpl<Client> dbExecutor = new DbExecutorImpl<>();
    JdbcMapper<Client, Long> jdbcMapperClient = new JdbcMapperImpl<>(Client.class, sessionManager,
        dbExecutor);
    Dao clientDao = new ClientDaoJdbcMapper(jdbcMapperClient);
    serviceClient = new DbServiceClientImpl(clientDao);
  }

  @Test
  @DisplayName("Должен корректно сохранять")
  void insertClient() {
    assertThat(serviceClient.findAll()).isEmpty();
    Client vasya = new Client(0,"Вася Пупкин", 15);
    long vasyaId = serviceClient.save(vasya);
    assertThat(serviceClient.findAll()).hasSize(1).containsOnly(vasya);
    assertThat(serviceClient.getById(vasyaId)).isPresent().get().isEqualTo(vasya);
    long vasyaId2 = serviceClient.save(vasya);
    assertThat(vasyaId2).isEqualTo(vasyaId);
    assertThat(serviceClient.findAll()).hasSize(1).containsOnly(vasya);
    Client nevasya = new Client(0,"Не Вася", 21);
    long nevasyaId = serviceClient.save(nevasya);
    assertThat(serviceClient.findAll()).hasSize(2).containsOnly(vasya, nevasya);
    assertThat(serviceClient.getById(nevasyaId)).isPresent().get().isEqualTo(nevasya);
  }

  @Test
  @DisplayName("Должен корректно обновлять")
  void updateClient() {
    assertThat(serviceClient.findAll()).isEmpty();
    Client vasya = new Client(0,"Вася Пупкин", 15);
    long vasyaId = serviceClient.save(vasya);
    assertThat(serviceClient.findAll()).hasSize(1).containsOnly(vasya);
    assertThat(serviceClient.getById(vasyaId)).isPresent().get().isEqualTo(vasya);
    Client nevasya = new Client(vasyaId,"Не Вася", 21);
    long nevasyaId = serviceClient.save(nevasya);
    assertThat(nevasyaId).isEqualTo(vasyaId);
    assertThat(serviceClient.findAll()).hasSize(1).containsOnly(nevasya);
    assertThat(serviceClient.getById(nevasyaId)).isNotEmpty().contains(nevasya);
    assertThat(serviceClient.getById(vasyaId)).isPresent().get().isEqualTo(nevasya);
  }

}
