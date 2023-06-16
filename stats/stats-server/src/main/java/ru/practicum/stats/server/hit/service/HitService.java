package ru.practicum.stats.server.hit.service;

import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.StatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface HitService {
    HitDto createEndpointHit(HitDto endpointHitDto);
    List<StatsDto> getEndpointHits(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
