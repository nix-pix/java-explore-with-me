package ru.practicum.stats.server.hit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.stats.server.hit.entity.Hit;
import ru.practicum.stats.server.hit.entity.Stats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HitRepository extends JpaRepository<Hit, Integer> {

    @Query("SELECT new ru.practicum.stats.server.hit.entity.Stats(hit.app, hit.uri, COUNT(DISTINCT hit.ip)) " +
            "FROM Hit hit " +
            "WHERE hit.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY hit.app, hit.uri " +
            "ORDER BY COUNT(DISTINCT hit.ip) DESC")
    List<Stats> getAllStatsDistinctIp(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.stats.server.hit.entity.Stats(hit.app, hit.uri, COUNT(hit.ip)) " +
            "FROM Hit hit " +
            "WHERE hit.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY hit.app, hit.uri " +
            "ORDER BY COUNT(hit.ip) DESC")
    List<Stats> getAllStats(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.stats.server.hit.entity.Stats(hit.app, hit.uri, COUNT(DISTINCT hit.ip)) " +
            "FROM Hit hit " +
            "WHERE hit.timestamp BETWEEN ?1 AND ?2 " +
            "AND hit.uri IN (?3) " +
            "GROUP BY hit.app, hit.uri " +
            "ORDER BY COUNT(DISTINCT hit.ip) DESC")
    List<Stats> getStatsByUrisDistinctIp(LocalDateTime start, LocalDateTime end, List<String> uri);

    @Query("SELECT new ru.practicum.stats.server.hit.entity.Stats(hit.app, hit.uri, COUNT(hit.ip)) " +
            "FROM Hit hit " +
            "WHERE hit.timestamp BETWEEN ?1 AND ?2 " +
            "AND hit.uri IN (?3) " +
            "GROUP BY hit.app, hit.uri " +
            "ORDER BY COUNT(hit.ip) DESC")
    List<Stats> getStatsByUris(LocalDateTime start, LocalDateTime end, List<String> uri);
}
