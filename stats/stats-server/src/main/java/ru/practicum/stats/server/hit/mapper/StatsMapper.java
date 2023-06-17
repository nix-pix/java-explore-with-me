package ru.practicum.stats.server.hit.mapper;

import ru.practicum.stats.dto.StatsDto;
import ru.practicum.stats.server.hit.model.Stats;

public class StatsMapper {

    public static StatsDto toViewStatsDto(Stats stats) {
        return StatsDto.builder()
                .hits(stats.getHits())
                .uri(stats.getUri())
                .app(stats.getApp())
                .build();
    }
}
