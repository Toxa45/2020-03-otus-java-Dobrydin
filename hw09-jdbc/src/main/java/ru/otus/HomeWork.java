package ru.otus;

import java.util.Optional;
import java.util.UUID;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.Dao;
import ru.otus.core.model.Account;
import ru.otus.core.model.Client;
import ru.otus.core.service.impl.DbServiceAccountImpl;
import ru.otus.core.service.impl.DbServiceClientImpl;
import ru.otus.config.HWDataSource;
import ru.otus.jdbc.DbExecutorImpl;
import ru.otus.jdbc.dao.AccountDaoJdbcMapper;
import ru.otus.jdbc.dao.ClientDaoJdbcMapper;
import ru.otus.jdbc.mapper.JdbcMapper;
import ru.otus.jdbc.mapper.impl.JdbcMapperImpl;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;


public class HomeWork {

  private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

  public static void main(String[] args) {
// Общая часть
    var dataSource = new HWDataSource();
    flywayMigrations(dataSource);
    var sessionManager = new SessionManagerJdbc(dataSource);

// Работа с пользователем
    DbExecutorImpl<Client> dbExecutor = new DbExecutorImpl<>();
    JdbcMapper<Client, Long> jdbcMapperClient = new JdbcMapperImpl<>(Client.class, sessionManager,
        dbExecutor); //
    Dao clientDao = new ClientDaoJdbcMapper(jdbcMapperClient);

// Код дальше должен остаться, т.е. clientDao должен использоваться
    var dbServiceClient = new DbServiceClientImpl(clientDao);
    var id = dbServiceClient.save(new Client(0, "dbServiceClient", 25));
    Optional<Client> clientOptional = dbServiceClient.getById(id);
    id = dbServiceClient.save(clientOptional.get());
    clientOptional.ifPresentOrElse(
        client -> logger.info("created client, name:{}", client.getName()),
        () -> logger.info("client was not created")
    );

//Загрузка всех клиентов
    logger.info("clients all");
    dbServiceClient.findAll()
        .forEach(client -> logger.info("client in ListAll: {}",client)
        );

// Работа со счетом
    DbExecutorImpl<Account> dbExecutorAccount = new DbExecutorImpl<>();
    JdbcMapper<Account, String> jdbcMapperAccount = new JdbcMapperImpl<>(Account.class,
        sessionManager,
        dbExecutorAccount); //
    Dao accountDao = new AccountDaoJdbcMapper(jdbcMapperAccount);

// Код дальше должен остаться, т.е. clientDao должен использоваться
    var dbServiceAccount = new DbServiceAccountImpl(accountDao);
    var idAccount = dbServiceAccount
        .save(new Account("d291c467-f4bf-47e5-856c-9e6b90d20c69", "VIP", 101.663));
    Optional<Account> accountOptionalOptional = dbServiceAccount.getById(idAccount);

    dbServiceAccount.save(new Account(UUID.randomUUID().toString(), "VIP", Math.random()*1000));
    accountOptionalOptional.ifPresentOrElse(
        account -> logger.info("created account, no:{}", account.getNo()),
        () -> logger.info("account was not created")
    );

    logger.info("account all");
    dbServiceAccount.findAll()
        .forEach(account -> logger.info("account in ListAll: {}",account)
        );
  }

  private static void flywayMigrations(DataSource dataSource) {
    logger.info("db migration started...");
    var flyway = Flyway.configure()
        .dataSource(dataSource)
        .locations("classpath:/db/migration")
        .load();
    flyway.migrate();
    logger.info("db migration finished.");
    logger.info("***");
  }
}
