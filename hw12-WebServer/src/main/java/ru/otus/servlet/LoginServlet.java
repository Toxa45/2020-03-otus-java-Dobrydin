package ru.otus.servlet;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static ru.otus.servlet.Authorization.TokenConfig.MAX_INACTIVE_INTERVAL;
import static ru.otus.servlet.Authorization.TokenConfig.SECRET;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import ru.otus.model.Role;
import ru.otus.model.User;
import ru.otus.services.TemplateProcessor;
import ru.otus.servlet.Authorization.UserAuthService;

public class LoginServlet extends HttpServlet {

  private static final String PARAM_LOGIN = "login";
  private static final String PARAM_PASSWORD = "password";
  private static final String LOGIN_PAGE_TEMPLATE = "login.html";


  private final TemplateProcessor templateProcessor;
  private final UserAuthService userAuthService;

  public LoginServlet(TemplateProcessor templateProcessor, UserAuthService userAuthService) {
    this.userAuthService = userAuthService;
    this.templateProcessor = templateProcessor;
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html");
    response.getWriter()
        .println(templateProcessor.getPage(LOGIN_PAGE_TEMPLATE, Collections.emptyMap()));
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    String name = request.getParameter(PARAM_LOGIN);
    String password = request.getParameter(PARAM_PASSWORD);

    User authenticate = userAuthService.getUserAuth(name, password);
    if (authenticate == null) {
      response.setStatus(SC_UNAUTHORIZED);
    } else {
      HttpSession session = request.getSession();

      Map<String, Object> tokenData = new HashMap<>();
      tokenData.put("role", authenticate.getRole());

      String token = Jwts.builder()
          .setClaims(tokenData)
          .setIssuedAt(new Date(System.currentTimeMillis()))
          .setExpiration(new Date(System.currentTimeMillis() + MAX_INACTIVE_INTERVAL*1000))
          .signWith(SignatureAlgorithm.HS512, SECRET)
          .compact();

      session.setAttribute("token", token);
      session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
      if (Role.ROLE_ADMIN.equals(authenticate.getRole())) {
        response.sendRedirect("/admin");
      } else {
        response.sendRedirect("/users");
      }


    }

  }

}
