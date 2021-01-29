package ru.otus.server;

import com.google.gson.Gson;
import java.util.List;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.service.UserService;
import ru.otus.services.TemplateProcessor;
import ru.otus.servlet.Authorization.UserAuthService;
import ru.otus.servlet.Authorization.AuthorizationFilter;
import ru.otus.servlet.Authorization.RoleMatcherURI;
import ru.otus.servlet.LoginServlet;

public class UsersWebServerWithFilterBasedSecurity extends UsersWebServerSimple {
    private final UserAuthService authService;

    public UsersWebServerWithFilterBasedSecurity(int port,
                                                 UserAuthService authService,
                                                  UserService userService,
                                                 Gson gson,
                                                 TemplateProcessor templateProcessor) {
        super(port, userService, gson, templateProcessor);
        this.authService = authService;;
    }

    @Override
    protected Handler applySecurity(ServletContextHandler servletContextHandler,
        List<RoleMatcherURI> roleMatcherURLS) {
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor, authService)), "/login");
        AuthorizationFilter authorizationFilter = new AuthorizationFilter(roleMatcherURLS);
       roleMatcherURLS.forEach(roleMatcherURL -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), roleMatcherURL.getUri(), null));
        return servletContextHandler;
    }
}
