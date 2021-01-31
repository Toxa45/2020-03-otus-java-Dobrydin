package ru.otus.servlet.Authorization;

import ru.otus.model.User;
import ru.otus.service.UserService;

public class UserAuthServiceImpl implements UserAuthService {

  private final UserService userService;

  public UserAuthServiceImpl(UserService userService) {
    this.userService = userService;
  }

  @Override
  public User getUserAuth(String login, String password) {
    User user = userService.findByLogin(login).orElse(null);
    if (user != null && user.getPassword().equals(password)) {
      return user;
    }
    return null;
  }

}
