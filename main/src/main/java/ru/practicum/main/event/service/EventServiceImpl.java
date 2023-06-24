package ru.practicum.main.event.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.category.entity.Category;
import ru.practicum.main.category.exception.CategoryNotExistException;
import ru.practicum.main.category.repository.CategoryRepository;
import ru.practicum.main.event.dto.*;
import ru.practicum.main.event.entity.Event;
import ru.practicum.main.event.enums.EventState;
import ru.practicum.main.event.enums.SortValue;
import ru.practicum.main.event.exception.EventCanceledException;
import ru.practicum.main.event.exception.EventNotExistException;
import ru.practicum.main.event.exception.EventPublishedException;
import ru.practicum.main.event.exception.EventWrongTimeException;
import ru.practicum.main.event.mapper.EventMapper;
import ru.practicum.main.event.repository.EventRepository;
import ru.practicum.main.user.entity.User;
import ru.practicum.main.user.exception.UserNotExistException;
import ru.practicum.main.user.repository.UserRepository;
import ru.practicum.main.util.Pattern;
import ru.practicum.stats.client.StatsClient;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.StatsDto;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.parse;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.PageRequest.of;
import static ru.practicum.main.event.enums.EventState.*;
import static ru.practicum.main.event.enums.SortValue.EVENT_DATE;
import static ru.practicum.main.event.enums.StateActionForAdmin.PUBLISH_EVENT;
import static ru.practicum.main.event.enums.StateActionForAdmin.REJECT_EVENT;
import static ru.practicum.main.event.enums.StateActionForUser.SEND_TO_REVIEW;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;
    private final EventMapper eventMapper;
    private final StatsClient statsClient;

    @Override
    @Transactional
    public LongEventDto saveEvent(Long userId,
                                  SavedEventDto savedEventDto) {
        Category category = categoryRepository.findById(savedEventDto.getCategory()).orElseThrow(
                () -> new CategoryNotExistException("This category does not exist"));
        LocalDateTime eventDate = savedEventDto.getEventDate();

        if (eventDate.isBefore(now().plusHours(2)))
            throw new EventWrongTimeException("EventDate should be in future");

        Event event = eventMapper.toEvent(savedEventDto);
        event.setCategory(category);

        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotExistException("User#" + userId + " does not exist"));

        event.setInitiator(user);

        return eventMapper.toLongEventDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    public LongEventDto updateEvent(Long eventId,
                                    UpdateEventAdminDto updateEventAdminDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new EventNotExistException("Event#" + eventId + " does not exist"));

        if (updateEventAdminDto == null)
            return eventMapper.toLongEventDto(event);

        if (updateEventAdminDto.getAnnotation() != null)
            event.setAnnotation(updateEventAdminDto.getAnnotation());

        if (updateEventAdminDto.getCategory() != null) {
            Category category = categoryRepository.findById(updateEventAdminDto.getCategory()).orElseThrow(
                    () -> new CategoryNotExistException("This category does not exist"));
            event.setCategory(category);
        }
        if (updateEventAdminDto.getDescription() != null)
            event.setDescription(updateEventAdminDto.getDescription());

        if (updateEventAdminDto.getLocation() != null)
            event.setLocation(updateEventAdminDto.getLocation());

        if (updateEventAdminDto.getPaid() != null)
            event.setPaid(updateEventAdminDto.getPaid());

        if (updateEventAdminDto.getParticipantLimit() != null)
            event.setParticipantLimit(updateEventAdminDto.getParticipantLimit().intValue());

        if (updateEventAdminDto.getRequestModeration() != null)
            event.setRequestModeration(updateEventAdminDto.getRequestModeration());

        if (updateEventAdminDto.getTitle() != null)
            event.setTitle(updateEventAdminDto.getTitle());

        if (updateEventAdminDto.getStateAction() != null) {
            if (PUBLISH_EVENT.equals(updateEventAdminDto.getStateAction())) {
                if (event.getPublishedOn() != null)
                    throw new EventPublishedException("Event has been published");
                if (CANCELED.equals(event.getState()))
                    throw new EventCanceledException("Event has been canceled");
                event.setState(PUBLISHED);
                event.setPublishedOn(now());
            } else if (REJECT_EVENT.equals(updateEventAdminDto.getStateAction())) {
                if (event.getPublishedOn() != null)
                    throw new EventPublishedException("Event has been published");
                event.setState(CANCELED);
            }
        }
        if (updateEventAdminDto.getEventDate() != null) {
            LocalDateTime eventTime = updateEventAdminDto.getEventDate();
            if (eventTime.isBefore(now()))
                throw new EventWrongTimeException("Wrong time");

            event.setEventDate(updateEventAdminDto.getEventDate());
        }
        Event saved = eventRepository.save(event);

        return eventMapper.toLongEventDto(saved);
    }

    @Override
    @Transactional
    public LongEventDto updateEventByUser(Long userId,
                                          Long eventId,
                                          UpdateEventUserDto updateEventUserDto) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(
                () -> new EventNotExistException("Event#" + eventId + " does not exist"));

        if (event.getPublishedOn() != null)
            throw new EventPublishedException("Event has been published");

        if (updateEventUserDto == null)
            return eventMapper.toLongEventDto(event);

        if (updateEventUserDto.getAnnotation() != null)
            event.setAnnotation(updateEventUserDto.getAnnotation());

        if (updateEventUserDto.getCategory() != null) {
            Category category = categoryRepository.findById(updateEventUserDto.getCategory()).orElseThrow(
                    () -> new CategoryNotExistException("This category does not exist"));
            event.setCategory(category);
        }
        if (updateEventUserDto.getDescription() != null)
            event.setDescription(updateEventUserDto.getDescription());

        if (updateEventUserDto.getEventDate() != null) {
            LocalDateTime eventTime = updateEventUserDto.getEventDate();
            if (eventTime.isBefore(now().plusHours(2)))
                throw new EventWrongTimeException("Wrong time");
            event.setEventDate(updateEventUserDto.getEventDate());
        }
        if (updateEventUserDto.getLocation() != null)
            event.setLocation(updateEventUserDto.getLocation());

        if (updateEventUserDto.getPaid() != null)
            event.setPaid(updateEventUserDto.getPaid());

        if (updateEventUserDto.getParticipantLimit() != null)
            event.setParticipantLimit(updateEventUserDto.getParticipantLimit().intValue());

        if (updateEventUserDto.getRequestModeration() != null)
            event.setRequestModeration(updateEventUserDto.getRequestModeration());

        if (updateEventUserDto.getTitle() != null)
            event.setTitle(updateEventUserDto.getTitle());

        if (updateEventUserDto.getStateAction() != null) {
            if (SEND_TO_REVIEW.equals(updateEventUserDto.getStateAction()))
                event.setState(PENDING);
            else
                event.setState(CANCELED);
        }
        return eventMapper.toLongEventDto(eventRepository.save(event));
    }

    @Override
    public List<ShortEventDto> getEvents(Long userId,
                                         Integer from,
                                         Integer size) {
        List<Event> events = eventRepository.findAllByInitiatorId(userId, of(from, size)).toList();
        return eventMapper.toShortEventDtos(events);
    }

    @Override
    public LongEventDto getEventByUser(Long userId,
                                       Long eventId) {
        return eventMapper.toLongEventDto(eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(
                () -> new EventNotExistException("Event#" + eventId + " does not exist")));
    }

    @Override
    public LongEventDto getEvent(Long id,
                                 HttpServletRequest request) {
        Event event = eventRepository.findByIdAndPublishedOnIsNotNull(id).orElseThrow(
                () -> new EventNotExistException("Event#" + id + " does not exist"));
        addView(event);
        sendStats(event, request);
        return eventMapper.toLongEventDto(event);
    }

    @Override
    public List<LongEventDto> getEventsWithParamsByAdmin(List<Long> users,
                                                         EventState states,
                                                         List<Long> categories,
                                                         String rangeStart,
                                                         String rangeEnd,
                                                         Integer from,
                                                         Integer size) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = builder.createQuery(Event.class);
        Root<Event> root = query.from(Event.class);
        Predicate criteria = builder.conjunction();

        LocalDateTime start = rangeStart == null ? null : parse(rangeStart, ofPattern(Pattern.DATE_PATTERN));
        LocalDateTime end = rangeEnd == null ? null : parse(rangeEnd, ofPattern(Pattern.DATE_PATTERN));

        if (rangeStart != null)
            criteria = builder.and(criteria, builder.greaterThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), start));

        if (rangeEnd != null)
            criteria = builder.and(criteria, builder.lessThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), end));

        if (categories != null && categories.size() > 0)
            criteria = builder.and(criteria, root.get("category").in(categories));

        if (users != null && users.size() > 0)
            criteria = builder.and(criteria, root.get("initiator").in(users));

        if (states != null)
            criteria = builder.and(criteria, root.get("state").in(states));

        query.select(root).where(criteria);

        List<Event> events = entityManager.createQuery(query)
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();

        if (events.size() == 0) return new ArrayList<>();

        return eventMapper.toLongEventDtos(events);
    }

    @Override
    public List<LongEventDto> getEventsWithParamsByUser(String text,
                                                        List<Long> categories,
                                                        Boolean paid,
                                                        String rangeStart,
                                                        String rangeEnd,
                                                        Boolean available,
                                                        SortValue sort,
                                                        Integer from,
                                                        Integer size,
                                                        HttpServletRequest request) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = builder.createQuery(Event.class);
        Root<Event> root = query.from(Event.class);
        Predicate criteria = builder.conjunction();

        LocalDateTime start = rangeStart == null ? null : parse(rangeStart, ofPattern(Pattern.DATE_PATTERN));
        if (rangeStart != null && LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).isBefore(now())) {
            throw new EventWrongTimeException("Wrong time");
        }

        LocalDateTime end = rangeEnd == null ? null : parse(rangeEnd, ofPattern(Pattern.DATE_PATTERN));

        if (text != null) {
            criteria = builder.and(criteria, builder.or(
                    builder.like(
                            builder.lower(root.get("annotation")), "%" + text.toLowerCase() + "%"),
                    builder.like(
                            builder.lower(root.get("description")), "%" + text.toLowerCase() + "%")));
        }

        if (categories != null && categories.size() > 0)
            criteria = builder.and(criteria, root.get("category").in(categories));

        if (paid != null) {
            Predicate predicate;
            if (paid) predicate = builder.isTrue(root.get("paid"));
            else predicate = builder.isFalse(root.get("paid"));
            criteria = builder.and(criteria, predicate);
        }

        if (rangeEnd != null)
            criteria = builder.and(criteria, builder.lessThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), end));

        if (rangeStart != null)
            criteria = builder.and(criteria, builder.greaterThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), start));

        query.select(root).where(criteria).orderBy(builder.asc(root.get("eventDate")));

        List<Event> events = entityManager.createQuery(query)
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();

        if (available)
            events = events.stream()
                    .filter((event -> event.getConfirmedRequests() < (long) event.getParticipantLimit()))
                    .collect(toList());

        if (sort != null) {
            if (EVENT_DATE.equals(sort))
                events = events.stream()
                        .sorted(comparing(Event::getEventDate))
                        .collect(toList());
            else
                events = events.stream()
                        .sorted(comparing(Event::getViews))
                        .collect(toList());
        }
        if (events.size() == 0) return new ArrayList<>();

        sendStats(events, request);
        return eventMapper.toLongEventDtos(events);
    }

    private List<StatsDto> getStats(String start,
                                    String end,
                                    List<String> uris) {
        return statsClient.getStats(start, end, uris, false).getBody();
    }

    private void addView(Event event) {
        String start = event.getCreatedOn().format(ofPattern(Pattern.DATE_PATTERN));
        String end = now().format(ofPattern(Pattern.DATE_PATTERN));
        List<String> uris = List.of("/events/" + event.getId());
        List<StatsDto> stats = getStats(start, end, uris);

        if (stats.size() == 1)
            event.setViews(stats.get(0).getHits());
        else
            event.setViews(1L);
    }

    private void sendStats(Event event,
                           HttpServletRequest request) {
        LocalDateTime now = now();
        HitDto requestDto = HitDto.builder()
                .ip(request.getRemoteAddr())
                .app("main")
                .uri("/events")
                .timestamp(now.format(ofPattern(Pattern.DATE_PATTERN)))
                .build();

        statsClient.addStats(requestDto);
        sendStatsForTheEvent(
                event.getId(),
                request.getRemoteAddr(),
                now
        );
    }

    private void sendStats(List<Event> events,
                           HttpServletRequest request) {
        LocalDateTime now = now();
        HitDto requestDto = HitDto.builder()
                .ip(request.getRemoteAddr())
                .app("main")
                .uri("/events")
                .timestamp(now.format(ofPattern(Pattern.DATE_PATTERN)))
                .build();

        statsClient.addStats(requestDto);
        sendStatsForEveryEvent(
                events,
                request.getRemoteAddr(),
                now()
        );
    }

    private void sendStatsForTheEvent(Long eventId,
                                      String remoteAddress,
                                      LocalDateTime now) {
        HitDto requestDto = HitDto.builder()
                .ip(remoteAddress)
                .app("main")
                .uri("/events/" + eventId)
                .timestamp(now.format(ofPattern(Pattern.DATE_PATTERN)))
                .build();

        statsClient.addStats(requestDto);
    }

    private void sendStatsForEveryEvent(List<Event> events,
                                        String remoteAddress,
                                        LocalDateTime now) {
        for (Event event : events) {
            HitDto requestDto = HitDto.builder()
                    .ip(remoteAddress)
                    .app("main")
                    .uri("/events/" + event.getId())
                    .timestamp(now.format(ofPattern(Pattern.DATE_PATTERN)))
                    .build();

            statsClient.addStats(requestDto);
        }
    }
}
