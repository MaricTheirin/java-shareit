package ru.practicum.shareit.user.model;

import lombok.Data;

@Data
public class User {

    private final long userId;
    private final String name;
    private final String email;

}
