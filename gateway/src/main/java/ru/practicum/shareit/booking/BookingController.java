package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.service.validation.Create;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

	private final BookingClient bookingClient;

	@PostMapping
	public ResponseEntity<Object> book(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @Validated({Create.class}) @RequestBody BookItemRequestDto bookingDto
	) {
        log.info("Пользователь с id={} создаёт бронь {}", bookingDto, userId);

        return bookingClient.bookItem(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> review(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable long bookingId,
            @RequestParam Boolean approved
    ) {
        log.info("Пользователь с id={} рассмотрел запрос на аренду с id={} результат={}", userId, bookingId, approved);

        return bookingClient.review(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> get(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable long bookingId
    ) {
        log.info("Пользователь с id={} запросил информацию о бронировании с id={}", userId, bookingId);

        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getOwnBookings(
            @RequestHeader("X-Sharer-User-Id")
            Long userId,
            @RequestParam(defaultValue = "ALL", name = "state")
            String stateParam,
            @PositiveOrZero(message = "Начальная позиция должна быть не меньше 0")
            @RequestParam(name = "from", defaultValue = "0")
            int from,
            @Positive(message = "Размер выдачи должен быть больше 0") @RequestParam(name = "size", defaultValue = "20")
            int size
    ) {
        log.info("Пользователь с id={} запросил свои бронирования в статусе {} с разбивкой [{},{}]",
                userId, stateParam, from, size
        );

        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));

        return bookingClient.findOwnBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findOwnItemsBookings(
            @RequestHeader("X-Sharer-User-Id")
            Long userId,
            @RequestParam(defaultValue = "ALL", name = "state")
            String stateParam,
            @PositiveOrZero(message = "Начальная позиция должна быть не меньше 0")
            @RequestParam(name = "from", defaultValue = "0")
            int from,
            @Positive(message = "Размер выдачи должен быть больше 0") @RequestParam(name = "size", defaultValue = "20")
            int size
    ) {
        log.info("Пользователь с id={} запросил бронь своих вещей в статусе {} с разбивкой [{},{}]",
                userId, stateParam, from, size
        );

        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        return bookingClient.findOwnItemsBookings(userId, state, from, size);
    }


}
