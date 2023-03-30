package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingShortResponseDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponseDto {

    private long id;

    private String name;

    private String description;

    private Boolean available;

    private BookingShortResponseDto lastBooking;

    private BookingShortResponseDto nextBooking;

    private List<CommentResponseDto> comments;

}
