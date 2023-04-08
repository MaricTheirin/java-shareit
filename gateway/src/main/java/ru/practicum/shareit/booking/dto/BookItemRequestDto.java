package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.shareit.service.validation.Create;
import ru.practicum.shareit.booking.validator.BookingStartBeforeEndValid;

@Getter
@AllArgsConstructor
@BookingStartBeforeEndValid
public class BookItemRequestDto {

	@NotNull(groups = Create.class, message = "Необходимо указать ID предмета")
	private final Long itemId;

	@NotNull(groups = Create.class, message = "Невозможно создать бронирование без даты начала")
	@FutureOrPresent(groups = Create.class, message = "Начало бронирования не может быть в прошлом")
	private final LocalDateTime start;

	@NotNull(groups = Create.class, message = "Невозможно создать бронирование без даты окончания")
	@Future(groups = Create.class, message = "Окончание бронирования не может быть в прошлом")
	private final LocalDateTime end;

}
