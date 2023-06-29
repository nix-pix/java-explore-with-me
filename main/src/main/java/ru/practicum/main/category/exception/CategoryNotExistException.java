package ru.practicum.main.category.exception;

public class CategoryNotExistException extends RuntimeException {

    public CategoryNotExistException(String message) {
        super(message);
    }
}
