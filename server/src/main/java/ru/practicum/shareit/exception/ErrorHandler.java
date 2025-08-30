package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)   // 404
    public ErrorResponse handleNotFoundException(NotFoundException e) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)   // 403
    public ErrorResponse handleForbiddenException(ForbiddenException e) {
        log.warn(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)   // 500
    public ErrorResponse handleThrowable(final Exception e) {
        log.warn("Error", e);
        return new ErrorResponse(e.getMessage());
    }
}
