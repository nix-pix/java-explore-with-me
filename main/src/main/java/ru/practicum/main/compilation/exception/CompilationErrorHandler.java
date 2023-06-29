package ru.practicum.main.compilation.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.main.error.Error;
import ru.practicum.main.util.Pattern;

import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class CompilationErrorHandler {

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public Error handleCompilationNotExistException(final CompilationNotExistException exception) {
        return Error.builder()
                .status(NOT_FOUND.getReasonPhrase().toUpperCase())
                .reason(("This compilation does not exist"))
                .message(exception.getMessage())
                .timestamp(now().format(ofPattern(Pattern.DATE_PATTERN)))
                .build();
    }
}
