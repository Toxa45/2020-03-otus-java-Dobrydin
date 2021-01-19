package ru.otus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.MyCache;
import ru.otus.core.dao.Dao;
import ru.otus.core.model.Account;
import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.Client;
import ru.otus.core.model.PhoneDataSet;
import ru.otus.core.service.impl.DbServiceAccountImpl;
import ru.otus.core.service.impl.DbServiceClientImpl;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.AccountHibernate;
import ru.otus.hibernate.dao.ClientDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;


public class HomeWork {

  private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

  public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

  public static void main(String[] args) {
// Общая часть
    Configuration configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

    SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class,
        PhoneDataSet.class, AddressDataSet.class, Account.class);
    SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
    Dao clientDao = new ClientDaoHibernate(sessionManager);

// Код дальше должен остаться, т.е. clientDao должен использоваться
    var dbServiceClient = new DbServiceClientImpl(clientDao,new MyCache<>());

    Client clientNew = new Client(0, "dbServiceClient", 25);
    AddressDataSet address = AddressDataSet.builder()
        .street("Курган")
        .client(clientNew)
        .build();
    clientNew.setAddress(address);
    var phone = new PhoneDataSet();
    phone.setNumber("1111");
    phone.setClient(clientNew);
    var phoneTwo = new PhoneDataSet();
    phoneTwo.setNumber("222");
    phoneTwo.setClient(clientNew);
    clientNew.setPhones(List.of(phone, phoneTwo));
    var id = dbServiceClient.save(clientNew);
    Optional<Client> clientOptional = dbServiceClient.getById(id);
    id = dbServiceClient.save(clientOptional.get());
    clientOptional.ifPresentOrElse(
        client -> logger.info("created client, name:{}", client.getName()),
        () -> logger.info("client was not created")
    );

//Загрузка всех клиентов
    logger.info("clients all");
    dbServiceClient.findAll()
        .forEach(client -> logger.info("client in ListAll: {}", client)
        );

// Работа со счетом
    Dao accountDao = new AccountHibernate(sessionManager);

// Код дальше должен остаться, т.е. clientDao должен использоваться
    var dbServiceAccount = new DbServiceAccountImpl(accountDao);
    var idAccount = dbServiceAccount
        .save(new Account("d291c467-f4bf-47e5-856c-9e6b90d20c69", "VIP", 101.663));
    Optional<Account> accountOptionalOptional = dbServiceAccount.getById(idAccount);

    dbServiceAccount.save(new Account(UUID.randomUUID().toString(), "VIP", Math.random() * 1000));
    accountOptionalOptional.ifPresentOrElse(
        account -> logger.info("created account, no:{}", account.getNo()),
        () -> logger.info("account was not created")
    );

    logger.info("account all");
    dbServiceAccount.findAll()
        .forEach(account -> logger.info("account in ListAll: {}", account)
        );
  }
}
