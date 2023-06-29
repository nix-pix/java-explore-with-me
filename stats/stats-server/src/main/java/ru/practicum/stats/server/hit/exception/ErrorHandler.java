package ru.practicum.stats.server.hit.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.stats.server.hit.entity.Error;
import ru.practicum.stats.server.hit.util.Pattern;

import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;

@RestControllerAdvice
public class ErrorHandler {

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public Error handleStatsBadTimeException(final StatsBadTimeException exception) {
        return Error.builder()
                .status(CONFLICT.getReasonPhrase().toUpperCase())
                .reason("Conditions are wrong")
                .message(exception.getMessage())
                .timestamp(now().format(ofPattern(Pattern.DATE_PATTERN)))
                .build();
    }
}
