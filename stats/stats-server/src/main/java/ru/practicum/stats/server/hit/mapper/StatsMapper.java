package ru.practicum.stats.server.hit.mapper;

import org.mapstruct.Mapper;
import ru.practicum.stats.dto.StatsDto;
import ru.practicum.stats.server.hit.entity.Stats;

@Mapper(componentModel = "spring")
public interface StatsMapper {
    StatsDto toDto(Stats viewStats);
}
