package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Long, Map<Long, Item>> items = new HashMap<>();
    private long lastItemId = 0;

    @Override
    public Item saveItem(Item item) {
        long ownerId = item.getOwnerId();
        item.setId(++lastItemId);
        if (!items.containsKey(ownerId)) {
            items.put(ownerId, new HashMap<>());
        }

        items.get(ownerId).put(item.getId(), item);
        return item;
    }

    @Override
    public Item getItem(Long itemId) {
        Optional<Item> item = items.values().stream()
                .map(Map::values)
                .flatMap(Collection::stream)
                .filter(elem -> elem.getId() == itemId)
                .findFirst();

        if (item.isPresent()) {
            return item.get();
        } else {
            throw new ItemNotFoundException("Запрошенная вещь не существует");
        }
    }

    @Override
    public Item updateItem(Item item) {
        items.get(item.getOwnerId()).put(item.getId(), item);
        return item;
    }

    @Override
    public Item deleteItem(Long userId, Long itemId) {
        return items.get(userId).remove(itemId);
    }

    @Override
    public List<Item> getAllItems(Long userId) {
        return new ArrayList<>(items.getOrDefault(userId, Collections.emptyMap()).values());
    }

    @Override
    public List<Item> getAvailableItems (String searchQuery) {

        if (searchQuery.length() == 0) {
            return Collections.emptyList();
        }

        String lowerCaseQuery = searchQuery.toLowerCase();
        Predicate<Item> itemFilter = item ->
                item.isAvailable() && (
                        item.getName().toLowerCase().contains(lowerCaseQuery) ||
                        item.getDescription().toLowerCase().contains(lowerCaseQuery)
        );

        return items.values().stream()
                .map(Map::values)
                .flatMap(Collection::stream)
                .filter(itemFilter)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isExist(Long itemId) {
        return items.containsKey(itemId);
    }

    @Override
    public boolean isExistAndBelongsToUser(Long userId, Long itemId) {
        return items.getOrDefault(userId, Collections.emptyMap()).containsKey(itemId);
    }

}
