package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.service.validation.Create;
import ru.practicum.shareit.service.validation.Update;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemDto {

    private final long id;

    @NotBlank(groups = Create.class, message = "Невозможно сохранить предмет с пустым наименованием")
    private final String name;

    @NotBlank(groups = {Create.class, Update.class}, message = "Невозможно сохранить предмет с пустым описанием")
    private final String description;

    @NotNull(groups = Create.class, message = "Невозможно сохранить предмет без указания его доступности")
    private final Boolean available;

    private BookingDto lastBooking;

    private BookingDto nextBooking;

    private List<CommentDto> comments;

}
