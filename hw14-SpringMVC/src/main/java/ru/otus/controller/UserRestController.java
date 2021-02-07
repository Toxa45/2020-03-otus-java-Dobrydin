package ru.otus.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.model.User;
import ru.otus.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserRestController {

  private final UserService userService;

  @GetMapping("/api/user/{id}")
  public User getUserById(@PathVariable(name = "id") long id) {
    return userService.getById(id).orElse(null);
  }

  @GetMapping("/api/user")
  public User getUserByName(@RequestParam(name = "name") String name) {
    return userService.findByLogin(name).orElse(null);
  }
}
