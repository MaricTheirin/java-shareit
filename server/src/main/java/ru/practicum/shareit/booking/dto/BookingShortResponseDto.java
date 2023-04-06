package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class BookingShortResponseDto {

    private final long id;

    private final long bookerId;

    private final LocalDateTime start;

    private final LocalDateTime end;

}
