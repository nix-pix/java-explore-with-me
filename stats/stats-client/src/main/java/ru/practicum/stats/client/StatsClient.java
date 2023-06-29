package ru.practicum.stats.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.StatsDto;

import java.util.List;

@Service
public class StatsClient {
    private final RestTemplate template;

    public StatsClient(@Value("${stats-server.url}") String url,
                       RestTemplateBuilder template) {
        this.template = template
                .uriTemplateHandler(new DefaultUriBuilderFactory(url))
                .build();
    }

    public void addStats(HitDto request) {
        template.postForEntity("/hit",
                new HttpEntity<>(request),
                HitDto.class);
    }

    public ResponseEntity<List<StatsDto>> getStats(String start,
                                                   String end,
                                                   List<String> uris,
                                                   boolean unique) {
        return template.exchange("/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                HttpMethod.GET,
                getHttpEntity(null),
                new ParameterizedTypeReference<>() {
                },
                start, end, uris, unique);
    }

    private <T> HttpEntity<T> getHttpEntity(T dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return dto == null ? new HttpEntity<>(headers) : new HttpEntity<>(dto, headers);
    }
}
