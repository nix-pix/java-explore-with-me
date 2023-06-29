package ru.practicum.stats.server.hit.repository;

import ru.practicum.stats.server.hit.entity.Stats;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository {

    List<Stats> getStats(LocalDateTime start,
                         LocalDateTime end,
                         List<String> uris,
                         boolean unique);
}
