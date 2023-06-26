package ru.practicum.main.category.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavedCategoryDto {
    @NotBlank
    @Length(max = 50)
    private String name;
}
