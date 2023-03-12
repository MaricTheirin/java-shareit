package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {

    User saveUser(User user);

    User getUser(Long userId);

    User updateUser(User user);

    User deleteUser(Long userId);

    List<User> getAllUsers();

    boolean isExist(Long userId);

    boolean isExist(String userEmail);

}
