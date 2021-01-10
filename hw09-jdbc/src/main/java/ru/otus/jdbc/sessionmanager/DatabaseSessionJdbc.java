package ru.otus.jdbc.sessionmanager;

import java.sql.Connection;
import ru.otus.core.sessionmanager.DatabaseSession;

public class DatabaseSessionJdbc implements DatabaseSession {
    private final Connection connection;

    DatabaseSessionJdbc(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }
}
