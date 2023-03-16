package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("UserRepositoryImpl")
public class UserRepositoryImpl implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private Long lastUserId = 0L;

    @Override
    public User save(User user) {
        user.setId(++lastUserId);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User get(Long userId) {
        return users.get(userId);
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User delete(Long userId) {
        return users.remove(userId);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean isExist(Long userId) {
        return users.containsKey(userId);
    }

    public boolean isExist(String userEmail) {
        return users.values().stream().anyMatch(user -> user.getEmail().equals(userEmail));
    }

}
