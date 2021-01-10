package ru.otus.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.jdbc.annotation.Id;

@Data
@AllArgsConstructor
public class Client {
    @Id
    private final long id;
    private final String name;
    private final int age;
}
