package ru.practicum.main.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.main.event.dto.LongEventDto;
import ru.practicum.main.event.dto.SavedEventDto;
import ru.practicum.main.event.dto.ShortEventDto;
import ru.practicum.main.event.entity.Event;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {
    @Mapping(source = "category", target = "category.id")
    Event toEvent(SavedEventDto savedEventDto);

    LongEventDto toLongEventDto(Event event);

    List<ShortEventDto> toShortEventDtos(List<Event> events);

    List<LongEventDto> toLongEventDtos(List<Event> events);
}
