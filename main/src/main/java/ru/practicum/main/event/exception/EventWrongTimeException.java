package ru.practicum.main.event.exception;

public class EventWrongTimeException extends RuntimeException {

    public EventWrongTimeException(String message) {
        super(message);
    }
}
