package ru.otus.model;

import lombok.Getter;

@Getter
public enum Role {
  ROLE_ANONYMOUS,
  ROLE_USER,
  ROLE_ADMIN;

  Role() {
  }
}
