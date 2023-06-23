package ru.practicum.main.request.service;

import ru.practicum.main.request.dto.RequestDto;
import ru.practicum.main.request.dto.RequestUpdateDto;
import ru.practicum.main.request.dto.RequestUpdateResult;

import java.util.List;

public interface RequestService {

    List<RequestDto> getCurrentUserRequests(Long userId);

    List<RequestDto> getRequestsByOwnerOfEvent(Long userId,
                                               Long eventId);

    RequestDto cancelRequest(Long userId,
                             Long requestId);

    RequestDto saveRequest(Long userId,
                           Long eventId);

    RequestUpdateResult updateRequests(Long userId,
                                       Long eventId,
                                       RequestUpdateDto requestUpdateDto);
}
