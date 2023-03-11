package ru.practicum.shareit.item.model;

import lombok.Data;

@Data
public class Item {

    final long itemId;
    final long ownerId;
    String name;
    String description;
    short shareCounter = 0;
    boolean isAvailable = true;

}
