package ru.practicum.main.compilation.mapper;

import org.mapstruct.Mapper;
import ru.practicum.main.compilation.dto.CompilationDto;
import ru.practicum.main.compilation.entity.Compilation;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompilationMapper {

    CompilationDto mapToCompilationDto(Compilation compilation);

    List<CompilationDto> mapToCompilationDtos(List<Compilation> compilations);
}
