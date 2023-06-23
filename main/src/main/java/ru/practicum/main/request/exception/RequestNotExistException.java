package ru.practicum.main.request.exception;

public class RequestNotExistException extends RuntimeException {

    public RequestNotExistException(String message) {
        super(message);
    }
}
