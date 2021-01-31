package ru.otus.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.model.User;
import ru.otus.service.UserService;

@RestController
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/user/{id}")
    public User getUserById(@PathVariable(name = "id") long id) {
        return userService.getById(id).orElse(null);
    }

    @GetMapping("/api/user")
    public User getUserByName(@RequestParam(name = "name") String name) {
        return userService.findByLogin(name).orElse(null);
    }

    @PostMapping("/api/user")
    public User saveUser(@RequestBody User user) {
        userService.save(user);
        return user;
    }


}
