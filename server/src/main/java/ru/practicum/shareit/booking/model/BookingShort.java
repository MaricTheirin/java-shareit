package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;

@Data
public class BookingShort {

    private final long id;

    private final long itemId;

    private final User booker;

    private final LocalDateTime start;

    private final LocalDateTime end;

}
