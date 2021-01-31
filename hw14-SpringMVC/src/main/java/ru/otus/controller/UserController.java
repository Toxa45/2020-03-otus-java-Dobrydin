package ru.otus.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.model.User;
import ru.otus.service.UserService;

/**
 * Created by dobrydin on 31.01.2021
 */
@Controller
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserController {
  private final UserService userService;

  @GetMapping({"/", "/user/list"})
  public String usersListView(Model model) {
    List<User> users = userService.findAll();
    model.addAttribute("users", users);
    return "usersList.html";
  }

  @GetMapping("/user/create")
  public String userCreateView(Model model) {
    model.addAttribute("user", new User());
    return "userCreate.html";
  }

  @PostMapping("/user/save")
  public RedirectView userSave(@ModelAttribute User user) {
    userService.save(user);
    return new RedirectView("/", true);
  }

}
