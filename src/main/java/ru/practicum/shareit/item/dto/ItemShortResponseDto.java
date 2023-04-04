package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemShortResponseDto {

    private final long id;

    private final Long requestId;

    private final String name;

    private final String description;

    private final boolean available;

    private final long ownerId;

}
