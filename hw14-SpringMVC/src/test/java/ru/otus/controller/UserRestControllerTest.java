package ru.otus.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.otus.model.Role;
import ru.otus.model.User;
import ru.otus.service.UserService;

/**
 * Created by dobrydin on 31.01.2021
 */
@ExtendWith(MockitoExtension.class)
class UserRestControllerTest {

  private MockMvc mvc;

  @Mock
  private UserService userService;

  @BeforeEach
  public void setUp() {
    mvc = MockMvcBuilders.standaloneSetup(new UserRestController(userService)).build();
  }

  @Test
  void getUserById() throws Exception {
    User expectedUser = new User(1, "Vasya","pypkin","pass", Role.ROLE_USER);
    Gson gson = new GsonBuilder().create();
    given(userService.getById(1L)).willReturn(Optional.of(expectedUser));
    mvc.perform(get("/api/user/{id}", 1L))
        .andExpect(status().isOk())
        .andExpect(content().string(gson.toJson(expectedUser)));
  }
}
