package ru.otus.servlet.Authorization;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.otus.model.Role;

/**
 * Created by dobrydin on 29.01.2021
 */
@RequiredArgsConstructor
@Data
public class RoleMatcherURI {

  private final String uri;
  private final Role role;
}
