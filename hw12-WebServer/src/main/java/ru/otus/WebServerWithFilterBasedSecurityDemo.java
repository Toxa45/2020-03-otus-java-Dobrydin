package ru.otus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.otus.dao.UserDao;
import ru.otus.helpers.HibernateConfigHelper;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.server.UsersWebServer;
import ru.otus.server.UsersWebServerWithFilterBasedSecurity;
import ru.otus.service.impl.DbServiceUserImpl;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.TemplateProcessorImpl;
import ru.otus.servlet.Authorization.UserAuthService;
import ru.otus.servlet.Authorization.UserAuthServiceImpl;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/users

    // Страница администраторов
    http://localhost:8080/admin

    // REST сервис
    http://localhost:8080/api/user/3
*/
public class WebServerWithFilterBasedSecurityDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {
        SessionManagerHibernate sessionManagerHibernate = HibernateConfigHelper
            .factorySessionManager();
        UserDao clientDao = new UserDaoHibernate(sessionManagerHibernate);

        var userService = new DbServiceUserImpl(clientDao);
        HibernateConfigHelper.populateDb(userService);

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(userService);

        UsersWebServer usersWebServer = new UsersWebServerWithFilterBasedSecurity(WEB_SERVER_PORT,
                authService, userService, gson, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
    }
}
