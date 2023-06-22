package ru.practicum.main.user.exception;

public class NameExistException extends RuntimeException {

    public NameExistException(String message) {
        super(message);
    }
}
