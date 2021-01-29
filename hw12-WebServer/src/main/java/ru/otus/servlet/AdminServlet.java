package ru.otus.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import ru.otus.model.Role;
import ru.otus.model.User;
import ru.otus.service.UserService;
import ru.otus.services.TemplateProcessor;


public class AdminServlet extends HttpServlet {

    private static final String ADMIN_PAGE_TEMPLATE = "admin.html";
    private static final String TEMPLATE_ATTR_RANDOM_USER = "userList";
    private final String NAME = "userName";
    private final String LOGIN = "userLogin";
    private final String PASSWORD = "userPassword";

    private final UserService userService;
    private final TemplateProcessor templateProcessor;
    private final Gson gson;

    public AdminServlet(TemplateProcessor templateProcessor, UserService userService, Gson gson) {
        this.templateProcessor = templateProcessor;
        this.userService = userService;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(TEMPLATE_ATTR_RANDOM_USER,userService.findAll());

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(ADMIN_PAGE_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var newUser = new User();
        newUser.setName(req.getParameter(NAME));
        newUser.setLogin(req.getParameter(LOGIN));
        newUser.setPassword(req.getParameter(PASSWORD));
        newUser.setRole(Role.ROLE_USER);
        userService.save(newUser);

        resp.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = resp.getOutputStream();
        out.print(gson.toJson(newUser));
    }

}
