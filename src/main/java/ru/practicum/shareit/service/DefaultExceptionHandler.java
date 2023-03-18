package ru.practicum.shareit.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.service.exception.ShareItException;
import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler({ShareItException.class})
    protected ResponseEntity<ExceptionMessage> handleShareItExceptions(
            ShareItException exception,
            HttpServletRequest request
    ) {
        return new ResponseEntity<>(new ExceptionMessage(exception, request.getRequestURI()), exception.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ExceptionMessage> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        String errorMessage = exception.getFieldErrors().stream()
                .map(error -> "'" + error.getField() + "': " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return new ResponseEntity<>(
                new ExceptionMessage("Ошибка при проверке полей: " + errorMessage, request.getRequestURI()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    protected ResponseEntity<ExceptionMessage> handleMissingRequestHeaderException(
            MissingRequestHeaderException exception,
            HttpServletRequest request
    ) {
        return new ResponseEntity<>(
                new ExceptionMessage("Нарушение безопасности", request.getRequestURI()),
                HttpStatus.BAD_REQUEST
        );
    }


    @ExceptionHandler({Throwable.class})
    protected ResponseEntity<ExceptionMessage> handleException(
            Throwable exception,
            HttpServletRequest request
    ) {
        return new ResponseEntity<>(
                new ExceptionMessage(exception, request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR
        );
    }


}
