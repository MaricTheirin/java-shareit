package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.user.User;

/**
 * TODO Sprint add-controllers.
 */

@Data
public class Item {

    final long itemId;
    final long ownerId;
    final String name;
    final String description;
    short shareCounter = 0;
    boolean isAvailable = true;

}
