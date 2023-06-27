package ru.practicum.stats.server.hit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.stats.server.hit.entity.Hit;

//@Repository
public interface StatsRepository extends JpaRepository<Hit, Long>, HitRepository {
}
