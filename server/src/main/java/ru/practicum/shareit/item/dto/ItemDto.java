package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemDto {

    private final long id;

    private final Long requestId;

    private final String name;

    private final String description;

    private final Boolean available;

}
