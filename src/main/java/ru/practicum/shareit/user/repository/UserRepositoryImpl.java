package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;

@Repository("UserRepositoryImpl")
public class UserRepositoryImpl implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User saveUser(User user) {
        users.put(user.getUserId(), user);
        return user;
    }

    @Override
    public User getUser(Long userId) {
        return users.get(userId);
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getUserId(), user);
        return user;
    }

    @Override
    public User deleteUser(Long userId) {
        return users.remove(userId);
    }

    @Override
    public boolean isExist(Long userId) {
        return users.containsKey(userId);
    }

}
