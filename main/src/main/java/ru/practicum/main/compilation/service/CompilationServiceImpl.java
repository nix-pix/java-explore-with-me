package ru.practicum.main.compilation.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.compilation.dto.CompilationDto;
import ru.practicum.main.compilation.dto.CompilationUpdateRequest;
import ru.practicum.main.compilation.dto.SavedCompilationDto;
import ru.practicum.main.compilation.entity.Compilation;
import ru.practicum.main.compilation.exception.CompilationNotExistException;
import ru.practicum.main.compilation.mapper.CompilationMapper;
import ru.practicum.main.compilation.repository.CompilationRepository;
import ru.practicum.main.event.entity.Event;
import ru.practicum.main.event.repository.EventRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public CompilationDto saveCompilation(SavedCompilationDto savedCompilationDto) {
        List<Event> events;

        if (savedCompilationDto.getEvents() != null) {
            events = eventRepository.findAllByIdIn(savedCompilationDto.getEvents());
        } else {
            events = new ArrayList<>();
        }

        Compilation compilation = Compilation.builder()
                .pinned(savedCompilationDto.getPinned() == null ? false : savedCompilationDto.getPinned())
                .title(savedCompilationDto.getTitle())
                .events(new HashSet<>(events))
                .build();

        Compilation saved = compilationRepository.save(compilation);
        return compilationMapper.mapToCompilationDto(saved);
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Long compId,
                                            CompilationUpdateRequest compilationUpdateRequest) {
        Compilation old = compilationRepository.findById(compId).orElseThrow(
                () -> new CompilationNotExistException("Compilation does not exist"));
        List<Long> eventsIds = compilationUpdateRequest.getEvents();
        if (eventsIds != null) {
            List<Event> events = eventRepository.findAllByIdIn(compilationUpdateRequest.getEvents());
            old.setEvents(new HashSet<>(events));
        }
        if (compilationUpdateRequest.getPinned() != null)
            old.setPinned(compilationUpdateRequest.getPinned());
        if (compilationUpdateRequest.getTitle() != null)
            old.setTitle(compilationUpdateRequest.getTitle());

        Compilation updated = compilationRepository.save(old);
        return compilationMapper.mapToCompilationDto(updated);
    }

    @Override
    public CompilationDto getCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new CompilationNotExistException("Compilation does not exist"));
        return compilationMapper.mapToCompilationDto(compilation);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned,
                                                Integer from,
                                                Integer size) {
        Predicate isPinned;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Compilation> query = criteriaBuilder.createQuery(Compilation.class);
        Root<Compilation> compilationRoot = query.from(Compilation.class);
        Predicate criteria = criteriaBuilder.conjunction();

        if (pinned != null) {
            if (pinned)
                isPinned = criteriaBuilder.isTrue(compilationRoot.get("pinned"));
            else
                isPinned = criteriaBuilder.isFalse(compilationRoot.get("pinned"));

            criteria = criteriaBuilder.and(criteria, isPinned);
        }
        query.select(compilationRoot).where(criteria);
        List<Compilation> compilations = entityManager.createQuery(query)
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();

        return compilationMapper.mapToCompilationDtos(compilations);
    }
}
