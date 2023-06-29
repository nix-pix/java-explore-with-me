package ru.practicum.main.category.mapper;

import org.mapstruct.Mapper;
import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.category.dto.SavedCategoryDto;
import ru.practicum.main.category.entity.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toCategory(SavedCategoryDto newCategoryDto);

    CategoryDto toCategoryDto(Category category);

    List<CategoryDto> toCategoryDtos(List<Category> categoryList);
}
