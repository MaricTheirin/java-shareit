package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
/**
 * TODO Sprint add-controllers.
 */

@Data
@AllArgsConstructor
public class ItemDto {

    private final long itemId;
    private final String name;
    private final String description;
    private short shareCounter;
    private final boolean isAvailable;

}
