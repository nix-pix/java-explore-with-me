package ru.practicum.main.event.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.main.error.Error;
import ru.practicum.main.util.Pattern;

import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class EventErrorHandler {

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public Error handleEventNotPublishedException(final EventNotPublishedException exception) {
        return Error.builder()
                .status(CONFLICT.getReasonPhrase().toUpperCase())
                .reason("Conditions are wrong")
                .message(exception.getMessage())
                .timestamp(now().format(ofPattern(Pattern.DATE_PATTERN)))
                .build();
    }

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public Error handleEventPublishedException(final EventPublishedException exception) {
        return Error.builder()
                .status(CONFLICT.getReasonPhrase().toUpperCase())
                .reason("Conditions are wrong")
                .message(exception.getMessage())
                .timestamp(now().format(ofPattern(Pattern.DATE_PATTERN)))
                .build();
    }

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public Error handleEventCanceledException(final EventCanceledException exception) {
        return Error.builder()
                .status(CONFLICT.getReasonPhrase().toUpperCase())
                .reason("Conditions are wrong")
                .message(exception.getMessage())
                .timestamp(now().format(ofPattern(Pattern.DATE_PATTERN)))
                .build();
    }

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public Error handleEventWrongTimeException(final EventWrongTimeException exception) {
        return Error.builder()
                .status(BAD_REQUEST.getReasonPhrase().toUpperCase())
                .reason("Wrong event time")
                .message(exception.getMessage())
                .timestamp(now().format(ofPattern(Pattern.DATE_PATTERN)))
                .build();
    }

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public Error handleEventNotExistException(final EventNotExistException exception) {
        return Error.builder()
                .status(NOT_FOUND.getReasonPhrase().toUpperCase())
                .reason(("This event does not exist"))
                .message(exception.getMessage())
                .timestamp(now().format(ofPattern(Pattern.DATE_PATTERN)))
                .build();
    }

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public Error handleBadRequestException(final EventBadRequestException exception) {
        return Error.builder()
                .status(BAD_REQUEST.getReasonPhrase().toUpperCase())
                .reason(("Incorrect parameters"))
                .message(exception.getMessage())
                .timestamp(now().format(ofPattern(Pattern.DATE_PATTERN)))
                .build();
    }
}
