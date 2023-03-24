package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {

    User save(User user);

    User get(Long userId);

    User update(User user);

    User delete(Long userId);

    List<User> findAll();

    boolean isExist(Long userId);

    boolean isExist(String userEmail);

}
