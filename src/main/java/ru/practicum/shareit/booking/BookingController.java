package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.service.validation.Create;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto book(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Validated({Create.class}) @RequestBody BookingDto bookingDto
    ) {
        return bookingService.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto review(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable long bookingId,
            @RequestParam Boolean approved
    ) {
        return bookingService.review(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto get(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable long bookingId
    ) {
        return bookingService.read(userId, bookingId);
    }

    @GetMapping
    public List<BookingResponseDto> getOwnBookings(
            @RequestHeader("X-Sharer-User-Id")
            Long userId,
            @RequestParam(defaultValue = "ALL")
            BookingState state,
            @PositiveOrZero(message = "Начальная позиция должна быть не меньше 0")
            @RequestParam(name = "from", defaultValue = "0")
            int from,
            @Positive(message = "Размер выдачи должен быть больше 0") @RequestParam(name = "size", defaultValue = "20")
            int size
    ) {
        return bookingService.findOwnBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> findOwnItemsBookings(
            @RequestHeader("X-Sharer-User-Id")
            Long userId,
            @RequestParam(defaultValue = "ALL")
            BookingState state,
            @PositiveOrZero(message = "Начальная позиция должна быть не меньше 0")
            @RequestParam(name = "from", defaultValue = "0")
            int from,
            @Positive(message = "Размер выдачи должен быть больше 0") @RequestParam(name = "size", defaultValue = "20")
            int size
    ) {
        return bookingService.findOwnItemsBookings(userId, state, from, size);
    }


}
