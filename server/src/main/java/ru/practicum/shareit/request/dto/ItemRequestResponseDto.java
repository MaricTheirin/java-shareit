package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemShortResponseDto;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemRequestResponseDto {

    private long id;

    private String description;

    private LocalDateTime created;

    private List<ItemShortResponseDto> items;

}
