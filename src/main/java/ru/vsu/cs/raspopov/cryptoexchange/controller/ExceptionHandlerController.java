package ru.vsu.cs.raspopov.cryptoexchange.controller;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String handleNoSuchElementException(NoSuchElementException e) {
        log.error("EXCEPTION OCCURRED", e);
        return e.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuilder builder = new StringBuilder();
        e.getBindingResult().getAllErrors().forEach((error) -> builder.append(error.getDefaultMessage()).append("\n"));
        log.error("EXCEPTION OCCURRED", e);
        return builder.toString();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public String handleAccessDeniedException(AccessDeniedException e) {
        log.error("EXCEPTION OCCURRED", e);
        return e.getMessage();
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleValidationException(ValidationException e) {
        log.error("EXCEPTION OCCURRED", e);
        return e.getMessage();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String  handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("EXCEPTION OCCURRED", e);
        return e.getMessage();
    }
}
