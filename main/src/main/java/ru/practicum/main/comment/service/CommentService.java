package ru.practicum.main.comment.service;

import ru.practicum.main.comment.dto.CommentDto;
import ru.practicum.main.comment.dto.SavedCommentDto;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {

    CommentDto saveComment(SavedCommentDto newCommentDto,
                           Long userId, Long eventId);

    CommentDto updateCommentByAdmin(SavedCommentDto newCommentDto,
                                    Long commentId);

    CommentDto updateCommentByUser(SavedCommentDto newCommentDto,
                                   Long userId,
                                   Long commentId);

    CommentDto getCommentByAdmin(Long commentId);

    CommentDto getCommentByUser(Long userId,
                                Long commentId);

    List<CommentDto> getCommentsByAdmin(Long eventId,
                                        Integer from,
                                        Integer size);

    List<CommentDto> getCommentsByUserAndTime(Long userId,
                                              LocalDateTime createdStart,
                                              LocalDateTime createdEnd,
                                              Integer from,
                                              Integer size);

    void deleteCommentByAdmin(Long commentId);

    void deleteCommentByUser(Long userId,
                             Long commentId);
}
