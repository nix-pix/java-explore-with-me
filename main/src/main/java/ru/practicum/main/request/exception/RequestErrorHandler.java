package ru.practicum.main.request.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.main.error.Error;
import ru.practicum.main.util.Pattern;

import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class RequestErrorHandler {

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public Error handleRequestExistException(final RequestExistException exception) {
        return Error.builder()
                .status(CONFLICT.getReasonPhrase().toUpperCase())
                .reason("Integrity constraint")
                .message(exception.getMessage())
                .timestamp(now().format(ofPattern(Pattern.DATE_PATTERN)))
                .build();
    }

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public Error handleRequestConfirmedException(final RequestConfirmedException exception) {
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
    public Error handleRequestParticipantLimitException(final RequestParticipantLimitException exception) {
        return Error.builder()
                .status(CONFLICT.getReasonPhrase().toUpperCase())
                .reason("Conditions are wrong")
                .message(exception.getMessage())
                .timestamp(now().format(ofPattern(Pattern.DATE_PATTERN)))
                .build();
    }

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public Error handleRequestNotExistException(final RequestNotExistException exception) {
        return Error.builder()
                .status(NOT_FOUND.getReasonPhrase().toUpperCase())
                .reason(("This request does not exist"))
                .message(exception.getMessage())
                .timestamp(now().format(ofPattern(Pattern.DATE_PATTERN)))
                .build();
    }
}
