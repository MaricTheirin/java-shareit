package ru.practicum.shareit.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.service.exception.ShareItException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler({ShareItException.class})
    protected ResponseEntity<ExceptionMessage> handleShareItExceptions(
            ShareItException exception,
            HttpServletRequest request
    ) {
        logException(exception, request);
        return new ResponseEntity<>(new ExceptionMessage(exception, request.getRequestURI()), exception.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ExceptionMessage> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        logException(exception, request);
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
        logException(exception, request);
        return new ResponseEntity<>(
                new ExceptionMessage("Нарушение безопасности", request.getRequestURI()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    protected ResponseEntity<ExceptionMessage> handleDataIntegrityViolationException(
            DataIntegrityViolationException exception,
            HttpServletRequest request
    ) {
        logException(exception, request);
        return new ResponseEntity<>(
                new ExceptionMessage("Нарушение корректности данных", request.getRequestURI()),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler({SQLException.class})
    protected ResponseEntity<ExceptionMessage> handleSQLException(
            SQLException exception,
            HttpServletRequest request
    ) {
        logException(exception, request);
        return new ResponseEntity<>(
                new ExceptionMessage("Ошибка SQL: " + exception.getMessage(), request.getRequestURI()),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ExceptionMessage> handleConstraintViolationException(
            ConstraintViolationException exception,
            HttpServletRequest request
    ) {
        logException(exception, request);
        String errorMessage = exception.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessageTemplate)
                .collect(Collectors.joining("; "));
        return new ResponseEntity<>(
                new ExceptionMessage("Ошибка при проверке: " + errorMessage, request.getRequestURI()),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ExceptionMessage> handleEntityNotFoundException(
            EntityNotFoundException exception,
            HttpServletRequest request
    ) {
        logException(exception, request);
        return new ResponseEntity<>(
                new ExceptionMessage("Запрошенная информация не обнаружена", request.getRequestURI()),
                HttpStatus.NOT_FOUND
        );
    }

    private void logException(Throwable throwable, HttpServletRequest request) {
        log.debug(
                "В ответ на запрос {}: {} выброшена ошибка: {}",
                request.getMethod(),
                request.getRequestURI(),
                throwable.getMessage());
    }


}
