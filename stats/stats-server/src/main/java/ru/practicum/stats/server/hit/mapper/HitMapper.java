package ru.practicum.stats.server.hit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.server.hit.entity.Hit;
import ru.practicum.stats.server.hit.util.Pattern;

@Mapper(componentModel = "spring")
public interface HitMapper {
    @Mapping(target = "timestamp", source = "timestamp", dateFormat = Pattern.DATE_PATTERN)
    Hit toEntity(HitDto hitDto);
}
