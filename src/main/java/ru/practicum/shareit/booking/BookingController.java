package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.service.validation.Create;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    protected BookingResponseDto book(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Validated({Create.class}) @RequestBody BookingDto bookingDto
    ) {
        return bookingService.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    protected BookingResponseDto review(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable long bookingId,
            @RequestParam Boolean approved
    ) {
        return bookingService.review(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    protected BookingResponseDto get(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId
    ) {
        return bookingService.read(userId, bookingId);
    }

    @GetMapping()
    protected List<BookingResponseDto> getOwnBookings(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "ALL") String state
    ) {
        return bookingService.findOwnBookings(userId, state);
    }

    @GetMapping("/owner")
    protected List<BookingResponseDto> findOwnItemsBookings(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "ALL") String state
    ) {
        return bookingService.findOwnItemsBookings(userId, state);
    }


}
