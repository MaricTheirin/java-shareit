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
	public ResponseEntity<Object> bookItem(
			@RequestHeader("X-Sharer-User-Id") long userId,
			@Validated(Create.class) @RequestBody BookItemRequestDto requestDto
	) {
		log.info("Пользователь с id={} создаёт бронь {}", requestDto, userId);
		return bookingClient.bookItem(userId, requestDto);
	}

	@PatchMapping(value = "/{bookingId}")
	public ResponseEntity<Object> review(
			@RequestHeader("X-Sharer-User-Id") Long userId,
			@PathVariable long bookingId,
			@RequestParam Boolean approved
	) {
		log.info("Пользователь с id={} рассмотрел запрос на аренду с id={} результат={}", userId, bookingId, approved);
		return bookingClient.review(userId, bookingId, approved);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBooking(
			@RequestHeader("X-Sharer-User-Id") long userId,
			@PathVariable Long bookingId
	) {
		log.info("Пользователь с id={} запросил информацию о бронировании с id={}", userId, bookingId);
		return bookingClient.getBooking(userId, bookingId);
	}

	@GetMapping
	public ResponseEntity<Object> getOwnBookings(
			@RequestHeader("X-Sharer-User-Id") long userId,
			@RequestParam(name = "state", defaultValue = "all") String stateParam,
			@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
			@Positive @RequestParam(name = "size", defaultValue = "10") Integer size
	) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));

		log.info("Пользователь с id={} запросил все бронирования в статусе {} с разбивкой [{},{}]",
				stateParam, userId, from, size
		);

		return bookingClient.getOwnBookings(userId, state, from, size);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> findOwnItemsBookings(
			@RequestHeader("X-Sharer-User-Id") Long userId,
			@RequestParam(defaultValue = "ALL") BookingState state,
			@RequestParam(name = "from", defaultValue = "0") int from,
			@RequestParam(name = "size", defaultValue = "20") int size
	) {
		return bookingClient.findOwnItemsBookings(userId, state, from, size);
	}



}
