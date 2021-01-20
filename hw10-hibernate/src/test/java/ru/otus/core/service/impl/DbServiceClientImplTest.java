package ru.otus.core.service.impl;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.core.dao.Dao;
import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.Client;
import ru.otus.core.model.PhoneDataSet;
import ru.otus.core.service.DBService;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.ClientDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

@DisplayName("Сервис для сохранения/обновления клиентов в БД")
class DbServiceClientImplTest {

  DBService<Client, Long> serviceClient;
  public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

  @BeforeEach
  void init() {
    Configuration configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

    SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class,
        PhoneDataSet.class, AddressDataSet.class);
    SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
    Dao clientDao = new ClientDaoHibernate(sessionManager);

    serviceClient = new DbServiceClientImpl(clientDao);
  }

  @Test
  @DisplayName("Должен корректно сохранять")
  void insertClient() {
    assertThat(serviceClient.findAll()).isEmpty();
    Client vasya = new Client(0, "Вася Пупкин", 15);
    long vasyaId = serviceClient.save(vasya);
    assertThat(serviceClient.findAll()).hasSize(1).containsOnly(vasya);
    assertThat(serviceClient.getById(vasyaId)).isPresent().get().isEqualTo(vasya);
    long vasyaId2 = serviceClient.save(vasya);
    assertThat(vasyaId2).isEqualTo(vasyaId);
    assertThat(serviceClient.findAll()).hasSize(1).containsOnly(vasya);
    Client nevasya = new Client(0, "Не Вася", 21);

    AddressDataSet address = AddressDataSet.builder()
        .street("Курган")
        .client(nevasya)
        .build();
    nevasya.setAddress(address);
    var phone = new PhoneDataSet();
    phone.setNumber("1111");
    phone.setClient(nevasya);
    var phoneTwo = new PhoneDataSet();
    phoneTwo.setNumber("222");
    phoneTwo.setClient(nevasya);
    nevasya.setPhones(List.of(phone, phoneTwo));

    long nevasyaId = serviceClient.save(nevasya);
    assertThat(serviceClient.findAll()).hasSize(2).containsOnly(vasya, nevasya);
    assertThat(serviceClient.getById(nevasyaId)).isPresent().get().isEqualTo(nevasya);
  }

  @Test
  @DisplayName("Должен корректно обновлять")
  void updateClient() {
    assertThat(serviceClient.findAll()).isEmpty();
    Client vasya = new Client(0, "Вася Пупкин", 15);
    long vasyaId = serviceClient.save(vasya);
    assertThat(serviceClient.findAll()).hasSize(1).containsOnly(vasya);
    assertThat(serviceClient.getById(vasyaId)).isPresent().get().isEqualTo(vasya);
    Client nevasya = new Client(vasyaId, "Не Вася", 21);

    AddressDataSet address = AddressDataSet.builder()
        .street("Курган")
        .client(nevasya)
        .build();
    nevasya.setAddress(address);
    var phone = new PhoneDataSet();
    phone.setNumber("1111");
    phone.setClient(nevasya);
    var phoneTwo = new PhoneDataSet();
    phoneTwo.setNumber("222");
    phoneTwo.setClient(nevasya);
    nevasya.setPhones(List.of(phone, phoneTwo));

    long nevasyaId = serviceClient.save(nevasya);
    assertThat(nevasyaId).isEqualTo(vasyaId);
    assertThat(serviceClient.findAll()).hasSize(1).containsOnly(nevasya);
    assertThat(serviceClient.getById(nevasyaId)).isNotEmpty().contains(nevasya);
    assertThat(serviceClient.getById(vasyaId)).isPresent().get().isEqualTo(nevasya);
  }

}
