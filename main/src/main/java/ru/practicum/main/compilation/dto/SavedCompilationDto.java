package ru.practicum.main.compilation.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavedCompilationDto {
    @NotBlank
    @Length(max = 50)
    private String title;
    private Boolean pinned;
    private List<Long> events;
}
