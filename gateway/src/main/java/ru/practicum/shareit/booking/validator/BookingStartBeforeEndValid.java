package ru.practicum.shareit.booking.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

//Помещаем аннотацию в javadoc поля
@Documented
//Список реализаций интерфейса
@Constraint(validatedBy = BookingStartBeforeEndValidator.class)
//Перечисляем то, что можно пометить аннотацией
@Target({ElementType.TYPE_USE})
//Указываем жизненный цикл аннотации
@Retention(RetentionPolicy.RUNTIME)

public @interface BookingStartBeforeEndValid {
    //Сообщение, выводимое при валидации
    String message() default "Период бронирования задан некорректно";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default {};
}
