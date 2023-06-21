package ru.practicum.stats.server.hit.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.StatsDto;
import ru.practicum.stats.server.hit.exception.StatsBadTimeException;
import ru.practicum.stats.server.hit.mapper.HitMapper;
import ru.practicum.stats.server.hit.mapper.StatsMapper;
import ru.practicum.stats.server.hit.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class HitServiceImpl implements HitService {
    private final StatsMapper viewStatsMapper;
    private final StatsRepository statsRepository;
    private final HitMapper hitMapper;

    @Override
    @Transactional
    public void saveHit(HitDto hitDto) {
        statsRepository.save(hitMapper.toEntity(hitDto));
    }

    @Override
    public List<StatsDto> getHits(LocalDateTime start,
                                  LocalDateTime end,
                                  List<String> uris,
                                  boolean unique) {
        if (start.isAfter(end))
            throw new StatsBadTimeException("Incorrect start or end time");
        return statsRepository.getStats(start, end, uris, unique)
                .stream()
                .map(viewStatsMapper::toDto)
                .collect(toList());
    }
}
