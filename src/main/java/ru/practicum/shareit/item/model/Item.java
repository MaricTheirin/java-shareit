package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.user.User;

/**
 * TODO Sprint add-controllers.
 */

@Data
public class Item {

    final User owner;
    final String name;
    final String description;
    boolean isAvailable;

}
