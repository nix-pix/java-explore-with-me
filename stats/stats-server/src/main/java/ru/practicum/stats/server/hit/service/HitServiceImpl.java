package ru.practicum.stats.server.hit.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.StatsDto;
import ru.practicum.stats.server.hit.mapper.StatsMapper;
import ru.practicum.stats.server.hit.storage.HitStorage;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static ru.practicum.stats.server.hit.mapper.HitMapper.toEndpointHit;
import static ru.practicum.stats.server.hit.mapper.HitMapper.toEndpointHitDto;

@Slf4j
@Service
public class HitServiceImpl implements HitService {
    private final HitStorage hitStorage;

    @Autowired
    public HitServiceImpl(HitStorage hitStorage) {
        this.hitStorage = hitStorage;
    }

    @Override
    public HitDto createEndpointHit(HitDto endpointHitDto) {
        return toEndpointHitDto(hitStorage.save(toEndpointHit(endpointHitDto)));
    }

    @Override
    public List<StatsDto> getEndpointHits(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (uris == null || uris.isEmpty()) {
            if (unique) {
                return hitStorage.getAllStatsDistinctIp(start, end).stream()
                        .map(StatsMapper::toViewStatsDto)
                        .collect(toList());
            } else {
                return hitStorage.getAllStats(start, end).stream()
                        .map(StatsMapper::toViewStatsDto)
                        .collect(toList());
            }
        } else {
            if (unique) {
                return hitStorage.getStatsByUrisDistinctIp(start, end, uris).stream()
                        .map(StatsMapper::toViewStatsDto)
                        .collect(toList());
            } else {
                return hitStorage.getStatsByUris(start, end, uris).stream()
                        .map(StatsMapper::toViewStatsDto)
                        .collect(toList());
            }
        }
    }
}
