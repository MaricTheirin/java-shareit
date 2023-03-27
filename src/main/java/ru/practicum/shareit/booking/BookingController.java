package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResultDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.service.validation.Create;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    protected BookingResultDto book(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Validated({Create.class}) @RequestBody BookingDto bookingDto
    ) {
        return bookingService.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    protected BookingResultDto review(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId,
            @RequestParam Boolean approved
    ) {
        return bookingService.review(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    protected BookingResultDto get(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId
    ) {
        return bookingService.read(userId, bookingId);
    }

    @GetMapping()
    protected List<BookingResultDto> getOwnBookings(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(required = false, defaultValue = "ALL") String state
    ) {
        return bookingService.findOwnBookings(userId, state);
    }

    @GetMapping("/owner")
    protected List<BookingResultDto> findOwnItemsBookings(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(required = false, defaultValue = "ALL") String state
    ) {
        return bookingService.findOwnItemsBookings(userId, state);
    }


}
