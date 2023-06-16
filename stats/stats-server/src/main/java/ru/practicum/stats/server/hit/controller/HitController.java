package ru.practicum.stats.server.hit.controller;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.StatsDto;
import ru.practicum.stats.server.hit.service.HitService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
public class HitController {
    private final HitService hitService;

    @PostMapping("/hit")
    public HitDto createEndpointHit(@RequestBody HitDto endpointHitDto) {
        return hitService.createEndpointHit(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<StatsDto> getEndpointHits(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                          @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                          @RequestParam(defaultValue = "false") boolean unique,
                                          @RequestParam(required = false) List<String> uris) {
        return hitService.getEndpointHits(start, end, uris, unique);
    }
}
