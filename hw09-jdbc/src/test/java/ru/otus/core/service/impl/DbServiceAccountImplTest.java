package ru.otus.core.service.impl;


import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.core.service.impl.FlywayMigrations.flywayMigrations;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.config.HWDataSource;
import ru.otus.core.dao.Dao;
import ru.otus.core.model.Account;
import ru.otus.core.service.DBService;
import ru.otus.jdbc.DbExecutorImpl;
import ru.otus.jdbc.dao.AccountDaoJdbcMapper;
import ru.otus.jdbc.mapper.JdbcMapper;
import ru.otus.jdbc.mapper.impl.JdbcMapperImpl;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

@DisplayName("Сервис для сохранения/обновления счетов в БД")
class DbServiceAccountImplTest {
  DBService<Account, String> serviceAccount;

  @BeforeEach
  void init() {
    var dataSource = new HWDataSource();
    flywayMigrations(dataSource);
    var sessionManager = new SessionManagerJdbc(dataSource);
    DbExecutorImpl<Account> dbExecutor = new DbExecutorImpl<>();
    JdbcMapper<Account, String> jdbcMapperAccount = new JdbcMapperImpl<>(Account.class, sessionManager,
        dbExecutor);
    Dao accountDao = new AccountDaoJdbcMapper(jdbcMapperAccount);
    serviceAccount = new DbServiceAccountImpl(accountDao);
  }

  @Test
  @DisplayName("Должен корректно сохранять")
  void insertAccount() {
    assertThat(serviceAccount.findAll()).isEmpty();
    Account vipAccount = new Account(UUID.randomUUID().toString(), "VIP", Math.random()*1000);
    String vipAccountId = serviceAccount.save(vipAccount);
    assertThat(serviceAccount.findAll()).hasSize(1).containsOnly(vipAccount);
    assertThat(serviceAccount.getById(vipAccountId)).isPresent().get().isEqualTo(vipAccount);
    String vipAccountId2 = serviceAccount.save(vipAccount);
    assertThat(vipAccountId2).isEqualTo(vipAccountId);
    assertThat(serviceAccount.findAll()).hasSize(1).containsOnly(vipAccount);
    Account childAccount = new Account(UUID.randomUUID().toString(), "child", Math.random()*10);
    String childAccountId = serviceAccount.save(childAccount);
    assertThat(serviceAccount.findAll()).hasSize(2).containsOnly(vipAccount, childAccount);
    assertThat(serviceAccount.getById(childAccountId)).isPresent().get().isEqualTo(childAccount);
  }

  @Test
  @DisplayName("Должен корректно обновлять")
  void updateAccount() {
    assertThat(serviceAccount.findAll()).isEmpty();
    Account vipAccount = new Account(UUID.randomUUID().toString(), "VIP", Math.random()*1000);
    String vipAccountId = serviceAccount.save(vipAccount);
    assertThat(serviceAccount.findAll()).hasSize(1).containsOnly(vipAccount);
    assertThat(serviceAccount.getById(vipAccountId)).isPresent().get().isEqualTo(vipAccount);
    Account childAccount = new Account(vipAccountId, "child", Math.random()*10);
    String childAccountId = serviceAccount.save(childAccount);
    assertThat(childAccountId).isEqualTo(vipAccountId);
    assertThat(serviceAccount.findAll()).hasSize(1).containsOnly(childAccount);
    assertThat(serviceAccount.getById(childAccountId)).isNotEmpty().contains(childAccount);
    assertThat(serviceAccount.getById(vipAccountId)).isPresent().get().isEqualTo(childAccount);
  }
}
