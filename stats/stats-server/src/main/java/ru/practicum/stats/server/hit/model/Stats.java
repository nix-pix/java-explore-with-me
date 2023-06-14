package ru.practicum.stats.server.hit.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Stats {
    private final String uri;
    private final String app;
    private final Long hits;
}
