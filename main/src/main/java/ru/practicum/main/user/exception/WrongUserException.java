package ru.practicum.main.user.exception;

public class WrongUserException extends RuntimeException {

    public WrongUserException(String message) {
        super(message);
    }
}
