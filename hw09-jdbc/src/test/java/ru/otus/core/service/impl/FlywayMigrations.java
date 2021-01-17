package ru.otus.core.service.impl;

import org.flywaydb.core.Flyway;
import ru.otus.config.HWDataSource;

public class FlywayMigrations {
  public static void flywayMigrations(HWDataSource dataSource) {
    var flyway = Flyway.configure()
        .dataSource(dataSource)
        .locations("classpath:/db/migration")
        .load();
    flyway.clean();
    flyway.migrate();
  }
}
