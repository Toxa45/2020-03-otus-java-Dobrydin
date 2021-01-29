package ru.otus.servlet.Authorization;

import ru.otus.model.User;

public interface UserAuthService {
    User getUserAuth(String login, String password);
}
