package ru.practicum.stats.server.hit.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.stats.server.hit.entity.Stats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HitRepository {

    List<Stats> getStats(LocalDateTime start,
                         LocalDateTime end,
                         List<String> uris,
                         boolean unique);
}
