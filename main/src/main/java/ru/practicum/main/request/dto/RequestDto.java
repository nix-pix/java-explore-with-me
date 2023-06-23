package ru.practicum.main.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.main.util.Pattern;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {
    private Long id;
    @JsonFormat(shape = STRING, pattern = Pattern.DATE_PATTERN)
    private LocalDateTime created;
    private Long requester;
    private String status;
    private Long event;
}
