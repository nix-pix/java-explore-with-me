package ru.practicum.stats.server.hit.entity;

import lombok.*;

import javax.persistence.Entity;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stats {
    private String uri;
    private String app;
    private Long hits;
}
