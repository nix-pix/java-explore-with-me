package ru.practicum.main.request.exception;

public class RequestParticipantLimitException extends RuntimeException {

    public RequestParticipantLimitException(String message) {
        super(message);
    }
}
