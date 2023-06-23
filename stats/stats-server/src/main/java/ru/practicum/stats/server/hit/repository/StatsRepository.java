package ru.practicum.stats.server.hit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.stats.server.hit.entity.Hit;

@Repository
public interface StatsRepository extends JpaRepository<Hit, Long>, HitRepository {
}
