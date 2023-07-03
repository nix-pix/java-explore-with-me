package ru.practicum.main.comment.exception;

public class CommentNotExistException extends RuntimeException {

    public CommentNotExistException(String message) {
        super(message);
    }
}
