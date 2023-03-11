package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Long, Map<Long, Item>> items = new HashMap<>();

    @Override
    public Item saveItem(Item item) {
        long ownerId = item.getOwnerId();
        if (!items.containsKey(ownerId)) {
            items.put(ownerId, new HashMap<>());
        }

        items.get(ownerId).put(item.getItemId(), item);
        return item;
    }

    @Override
    public Item getItem(Long userId, Long itemId) {
        return items.get(userId).get(itemId);
    }

    @Override
    public Item updateItem(Item item) {
        items.get(item.getOwnerId()).put(item.getItemId(), item);
        return item;
    }

    @Override
    public Item deleteItem(Long userId, Long itemId) {
        return items.get(userId).remove(itemId);
    }

    @Override
    public boolean isExist(Long itemId) {
        return items.containsKey(itemId);
    }
}
