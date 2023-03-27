package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.service.validation.Create;
import ru.practicum.shareit.service.validation.Update;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
public class BookingDto {

    @NotBlank(groups = Update.class)
    private final long id;

    @NotNull(groups = Create.class, message = "Необходимо указать ID предмета")
    private final Long itemId;

    @NotNull(groups = Create.class)
    @FutureOrPresent(groups = Create.class, message = "Начало бронирования не может быть в прошлом")
    private final LocalDateTime start;

    @NotNull(groups = Create.class)
    @Future(groups = Create.class, message = "Окончание бронирования не может быть в прошлом")
    private final LocalDateTime end;

    private final Long bookerId;

}
