package ru.otus.helpers;

import java.util.List;
import lombok.NoArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.flyway.MigrationsExecutorFlyway;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.model.Role;
import ru.otus.model.User;
import ru.otus.service.DBService;

@NoArgsConstructor
public final class HibernateConfigHelper {

  private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

  private static final Logger log = LoggerFactory.getLogger(HibernateConfigHelper.class);

  public static SessionManagerHibernate factorySessionManager(){
    Configuration configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
    String dbUrl = configuration.getProperty("hibernate.connection.url");
    String dbUserName = configuration.getProperty("hibernate.connection.username");
    String dbPassword = configuration.getProperty("hibernate.connection.password");

    new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

    SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(configuration, User.class);
    return new SessionManagerHibernate(sessionFactory);
  }


  public static void populateDb(DBService<User, Long> userService) {
    log.info("populateDb() - start");

    if (userService.findAll().isEmpty()) {
      List<User> users = List.of(
          new User(1L, "admin", "admin", "admin", Role.ROLE_ADMIN),
          new User(2L, "user2", "login2", "pass2",Role.ROLE_USER),
          new User(3L, "user3", "login3", "pass3",Role.ROLE_USER),
          new User(4L, "user4", "login4", "pass4",Role.ROLE_USER),
          new User(5L, "user5", "login5", "pass5",Role.ROLE_USER)
      );
      users.forEach(userService::save);
    }

    log.info("populateDb() - end");
  }
}
