package ru.practicum.main.comment.controller;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.comment.dto.CommentDto;
import ru.practicum.main.comment.dto.SavedCommentDto;
import ru.practicum.main.comment.service.CommentService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static ru.practicum.main.util.Pattern.DATE_PATTERN;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/users/{userId}")
public class CommentPrivateController {
    private final CommentService commentService;

    @ResponseStatus(CREATED)
    @PostMapping("/comments/{eventId}")
    public CommentDto saveComment(@RequestBody @Valid SavedCommentDto savedCommentDto,
                                  @PathVariable Long eventId,
                                  @PathVariable Long userId) {
        return commentService.saveComment(savedCommentDto, userId, eventId);
    }

    @PatchMapping("/comments/{commentId}")
    public CommentDto updateComment(@RequestBody @Valid SavedCommentDto savedCommentDto,
                                    @PathVariable Long commentId,
                                    @PathVariable Long userId) {
        return commentService.updateCommentByUser(savedCommentDto, userId, commentId);
    }

    @GetMapping("/comments")
    public List<CommentDto> getUserCommentsByTime(@RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime createdStart,
                                                  @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime createdEnd,
                                                  @RequestParam(required = false, defaultValue = "10") Integer size,
                                                  @RequestParam(required = false, defaultValue = "0") Integer from,
                                                  @PathVariable Long userId) {
        return commentService.getCommentsByUserAndTime(userId, createdStart, createdEnd, from, size);
    }

    @GetMapping("/comments/{commentId}")
    public CommentDto getCommentById(@PathVariable Long commentId,
                                     @PathVariable Long userId) {
        return commentService.getCommentByUser(userId, commentId);
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/comments/{commentId}")
    public void deleteCommentByUser(@PathVariable Long commentId,
                                    @PathVariable Long userId) {
        commentService.deleteCommentByUser(userId, commentId);
    }
}
