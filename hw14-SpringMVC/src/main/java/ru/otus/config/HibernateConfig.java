package ru.otus.config;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.flyway.MigrationsExecutorFlyway;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.model.User;

@Configuration
public class HibernateConfig {

  private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

  @Bean
  public static SessionManagerHibernate factorySessionManager(){
    var configuration = new org.hibernate.cfg.Configuration().configure(HIBERNATE_CFG_FILE);
    String dbUrl = configuration.getProperty("hibernate.connection.url");
    String dbUserName = configuration.getProperty("hibernate.connection.username");
    String dbPassword = configuration.getProperty("hibernate.connection.password");

    new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

    SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(configuration, User.class);

    return new SessionManagerHibernate(sessionFactory);
  }


}
