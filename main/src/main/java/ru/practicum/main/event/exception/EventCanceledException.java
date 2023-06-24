package ru.practicum.main.event.exception;

public class EventCanceledException extends RuntimeException {

    public EventCanceledException(String message) {
        super(message);
    }
}
