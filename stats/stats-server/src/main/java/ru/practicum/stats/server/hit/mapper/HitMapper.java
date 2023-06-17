package ru.practicum.stats.server.hit.mapper;

import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.server.hit.model.Hit;

public class HitMapper {

    public static Hit toEndpointHit(HitDto endpointHitDto) {
        return Hit.builder()
                .id(endpointHitDto.getId())
                .uri(endpointHitDto.getUri())
                .app(endpointHitDto.getApp())
                .ip(endpointHitDto.getIp())
                .timestamp(endpointHitDto.getTimestamp())
                .build();
    }

    public static HitDto toEndpointHitDto(Hit endpointHit) {
        return HitDto.builder()
                .id(endpointHit.getId())
                .uri(endpointHit.getUri())
                .app(endpointHit.getApp())
                .ip(endpointHit.getIp())
                .timestamp(endpointHit.getTimestamp())
                .build();
    }
}
