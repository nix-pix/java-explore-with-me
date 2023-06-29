package ru.practicum.main.compilation.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilationUpdateRequest {
    @Length(max = 50)
    private String title;
    private Boolean pinned;
    private List<Long> events;
}
