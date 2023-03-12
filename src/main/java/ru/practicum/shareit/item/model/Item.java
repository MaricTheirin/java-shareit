package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Item {

    private long itemId;
    private final long ownerId;
    private String name;
    private String description;
    private short shareCounter;
    private boolean available;

}
