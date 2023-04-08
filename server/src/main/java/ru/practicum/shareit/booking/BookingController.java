package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;


@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto bookItem(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody BookingDto bookingDto
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
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "ALL") BookingState state,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        return bookingService.findOwnBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> findOwnItemsBookings(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "ALL") BookingState state,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        return bookingService.findOwnItemsBookings(userId, state, from, size);
    }


}
