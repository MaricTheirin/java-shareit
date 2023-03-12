package ru.practicum.shareit.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.service.exception.ShareItException;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler({ShareItException.class})
    protected ResponseEntity<ExceptionMessage> handleShareItExceptions (
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
        return new ResponseEntity<>(new ExceptionMessage(exception, request.getRequestURI()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({Throwable.class})
    protected ResponseEntity<ExceptionMessage> handleException (
            Throwable exception,
            HttpServletRequest request
    ) {
        return new ResponseEntity<>(
                new ExceptionMessage(exception, request.getRequestURI()), HttpStatus.INTERNAL_SERVER_ERROR
        );
    }


}
