package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

public interface UserRepository {

    User saveUser(User user);

    User getUser(Long userId);

    User updateUser(User user);

    User deleteUser(Long userId);

    boolean isExist(Long userId);

}