package ru.practicum.main.request.mapper;

import org.mapstruct.Mapper;
import ru.practicum.main.request.dto.RequestDto;
import ru.practicum.main.request.entity.Request;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    RequestDto toRequestDto(Request request);

    List<RequestDto> toRequestDtos(List<Request> requests);
}
