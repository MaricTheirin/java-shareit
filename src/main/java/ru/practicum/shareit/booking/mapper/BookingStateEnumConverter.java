package ru.practicum.shareit.booking.mapper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.exception.BookingUnsupportedStateException;
import ru.practicum.shareit.booking.model.BookingState;

@Component
public class BookingStateEnumConverter implements Converter<String, BookingState> {
    @Override
    public BookingState convert(@Nullable String source) {
        try {
            return BookingState.valueOf(source);
        } catch (IllegalArgumentException e) {
            throw new BookingUnsupportedStateException("Unknown state: " + source);
        }
    }
}
