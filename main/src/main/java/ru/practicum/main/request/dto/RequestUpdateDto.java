package ru.practicum.main.request.dto;

import lombok.*;
import ru.practicum.main.request.enums.RequestUpdateStatus;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RequestUpdateDto {
    private RequestUpdateStatus status;
    private List<Long> requestIds;
}
