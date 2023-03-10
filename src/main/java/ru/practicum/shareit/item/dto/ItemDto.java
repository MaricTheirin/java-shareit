package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
/**
 * TODO Sprint add-controllers.
 */

@Data
@AllArgsConstructor
public class ItemDto {

    final long itemId;
    final String name;
    final String description;
    short shareCounter = 0;
    boolean isAvailable;

}
