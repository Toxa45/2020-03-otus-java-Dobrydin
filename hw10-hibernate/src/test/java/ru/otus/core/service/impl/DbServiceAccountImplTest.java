package ru.otus.core.service.impl;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.core.dao.Dao;
import ru.otus.core.model.Account;
import ru.otus.core.service.DBService;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.AccountHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

@DisplayName("Сервис для сохранения/обновления счетов в БД")
class DbServiceAccountImplTest {

  DBService<Account, String> serviceAccount;
  public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

  @BeforeEach
  void init() {
    Configuration configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

    SessionFactory sessionFactory = HibernateUtils
        .buildSessionFactory(configuration, Account.class);
    SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
    Dao accountDao = new AccountHibernate(sessionManager);

    serviceAccount = new DbServiceAccountImpl(accountDao);
  }

  @Test
  @DisplayName("Должен корректно сохранять")
  void insertAccount() {
    assertThat(serviceAccount.findAll()).isEmpty();
    Account vipAccount = new Account(UUID.randomUUID().toString(), "VIP", Math.random() * 1000);
    String vipAccountId = serviceAccount.save(vipAccount);
    assertThat(serviceAccount.findAll()).hasSize(1).containsOnly(vipAccount);
    assertThat(serviceAccount.getById(vipAccountId)).isPresent().get().isEqualTo(vipAccount);
    String vipAccountId2 = serviceAccount.save(vipAccount);
    assertThat(vipAccountId2).isEqualTo(vipAccountId);
    assertThat(serviceAccount.findAll()).hasSize(1).containsOnly(vipAccount);
    Account childAccount = new Account(UUID.randomUUID().toString(), "child", Math.random() * 10);
    String childAccountId = serviceAccount.save(childAccount);
    assertThat(serviceAccount.findAll()).hasSize(2).containsOnly(vipAccount, childAccount);
    assertThat(serviceAccount.getById(childAccountId)).isPresent().get().isEqualTo(childAccount);
  }

  @Test
  @DisplayName("Должен корректно обновлять")
  void updateAccount() {
    assertThat(serviceAccount.findAll()).isEmpty();
    Account vipAccount = new Account(UUID.randomUUID().toString(), "VIP", Math.random() * 1000);
    String vipAccountId = serviceAccount.save(vipAccount);
    assertThat(serviceAccount.findAll()).hasSize(1).containsOnly(vipAccount);
    assertThat(serviceAccount.getById(vipAccountId)).isPresent().get().isEqualTo(vipAccount);
    Account childAccount = new Account(vipAccountId, "child", Math.random() * 10);
    String childAccountId = serviceAccount.save(childAccount);
    assertThat(childAccountId).isEqualTo(vipAccountId);
    assertThat(serviceAccount.findAll()).hasSize(1).containsOnly(childAccount);
    assertThat(serviceAccount.getById(childAccountId)).isNotEmpty().contains(childAccount);
    assertThat(serviceAccount.getById(vipAccountId)).isPresent().get().isEqualTo(childAccount);
  }
}
