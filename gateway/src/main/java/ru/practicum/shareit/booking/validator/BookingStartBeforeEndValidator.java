package ru.practicum.shareit.booking.validator;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class BookingStartBeforeEndValidator implements ConstraintValidator<BookingStartBeforeEndValid, BookItemRequestDto> {
    @Override
    public void initialize(BookingStartBeforeEndValid constraintAnnotation) {
        //Метод может быть использован для того, чтобы сохранить данные из аннотации
    }

    @Override
    public boolean isValid(BookItemRequestDto value, ConstraintValidatorContext context) {
        if (value.getStart() == null || value.getEnd() == null) {
            log.warn("Описание бронирования должен содержать дату начала и окончания");
            return false;
        }
        log.warn("Дата начала бронирования {} не может быть позже даты окончания {}", value.getStart(), value.getEnd());
        return value.getStart().isBefore(value.getEnd());
    }
}
