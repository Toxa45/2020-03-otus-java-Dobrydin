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
  public SessionManagerHibernate factorySessionManager(
      org.hibernate.cfg.Configuration configurationHibernate,
      MigrationsExecutorFlyway migrationsExecutorFlyway) {
    migrationsExecutorFlyway.executeMigrations();
    SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(configurationHibernate, User.class);

    return new SessionManagerHibernate(sessionFactory);
  }

  @Bean
  public MigrationsExecutorFlyway migrationsExecutorFlyway(
      org.hibernate.cfg.Configuration configuration) {
    String dbUrl = configuration.getProperty("hibernate.connection.url");
    String dbUserName = configuration.getProperty("hibernate.connection.username");
    String dbPassword = configuration.getProperty("hibernate.connection.password");
    return new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword);
  }


  @Bean
  public org.hibernate.cfg.Configuration configurationHibernate() {
    return new org.hibernate.cfg.Configuration().configure(HIBERNATE_CFG_FILE);
  }

}
