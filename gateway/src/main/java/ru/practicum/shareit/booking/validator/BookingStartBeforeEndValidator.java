package ru.practicum.shareit.booking.validator;

import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BookingStartBeforeEndValidator implements ConstraintValidator<BookingStartBeforeEndValid, BookItemRequestDto> {
    @Override
    public void initialize(BookingStartBeforeEndValid constraintAnnotation) {
        //Метод может быть использован для того, чтобы сохранить данные из аннотации
    }

    @Override
    public boolean isValid(BookItemRequestDto value, ConstraintValidatorContext context) {
        return value.getStart().isBefore(value.getEnd());
    }
}
