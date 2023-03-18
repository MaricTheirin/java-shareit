package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Long, Map<Long, Item>> userItems = new HashMap<>();
    private final Map<Long, Item> items = new HashMap<>();
    private long lastItemId = 0;

    @Override
    public Item save(Item item) {
        long ownerId = item.getOwnerId();

        item.setId(++lastItemId);
        userItems.computeIfAbsent(ownerId, k -> new HashMap<>()).put(item.getId(), item);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item get(Long itemId) {
        if (items.containsKey(itemId)) {
            return items.get(itemId);
        } else {
            throw new ItemNotFoundException("Запрошенная вещь не существует");
        }
    }

    @Override
    public Item update(Item item) {
        userItems.get(item.getOwnerId()).put(item.getId(), item);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item delete(Long userId, Long itemId) {
        items.remove(itemId);
        return userItems.get(userId).remove(itemId);
    }

    @Override
    public List<Item> findAll(Long userId) {
        return new ArrayList<>(userItems.getOrDefault(userId, Collections.emptyMap()).values());
    }

    @Override
    public List<Item> findAvailable(String searchQuery) {

        if (searchQuery.length() == 0) {
            return Collections.emptyList();
        }

        String lowerCaseQuery = searchQuery.toLowerCase();
        Predicate<Item> itemFilter = item ->
                item.isAvailable() && (
                        item.getName().toLowerCase().contains(lowerCaseQuery) ||
                        item.getDescription().toLowerCase().contains(lowerCaseQuery)
        );

        return items.values().stream().filter(itemFilter).collect(Collectors.toList());
    }

    @Override
    public boolean isExist(Long itemId) {
        return items.containsKey(itemId);
    }

    @Override
    public boolean isExistAndBelongsToUser(Long userId, Long itemId) {
        return userItems.getOrDefault(userId, Collections.emptyMap()).containsKey(itemId);
    }

}
