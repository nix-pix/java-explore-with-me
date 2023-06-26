package ru.practicum.main.category.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.category.dto.SavedCategoryDto;
import ru.practicum.main.category.entity.Category;
import ru.practicum.main.category.exception.CategoryNotEmptyException;
import ru.practicum.main.category.exception.CategoryNotExistException;
import ru.practicum.main.category.mapper.CategoryMapper;
import ru.practicum.main.category.repository.CategoryRepository;
import ru.practicum.main.event.repository.EventRepository;
import ru.practicum.main.user.exception.NameExistException;

import java.util.List;

import static org.springframework.data.domain.PageRequest.of;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDto saveCategory(SavedCategoryDto savedCategoryDto) {
        if (categoryRepository.existsByName(savedCategoryDto.getName()))
            throw new NameExistException("Category with name " + savedCategoryDto.getName() + " cannot be saved");
        Category entity = categoryMapper.toCategory(savedCategoryDto);
        Category saved = categoryRepository.save(entity);
        return categoryMapper.toCategoryDto(saved);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category saved;
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new CategoryNotExistException("Category#" + id + " does not exist"));
        if (category.getName().equals(categoryDto.getName())) {
            category.setName(categoryDto.getName());
            saved = categoryRepository.save(category);
            return categoryMapper.toCategoryDto(saved);
        }
        if (categoryRepository.existsByName(categoryDto.getName()))
            throw new NameExistException("Category with name " + categoryDto.getName() + " cannot be updated");
        category.setName(categoryDto.getName());
        saved = categoryRepository.save(category);
        return categoryMapper.toCategoryDto(saved);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        if (eventRepository.existsByCategoryId(id))
            throw new CategoryNotEmptyException("Category#" + id + " is not empty");
        categoryRepository.deleteById(id);
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        return categoryMapper.toCategoryDtos(categoryRepository.findAll(of(from / size, size)).toList());
    }

    @Override
    public CategoryDto getCategory(Long id) {
        var category = categoryRepository.findById(id).orElseThrow(
                () -> new CategoryNotExistException("Category#" + id + " does not exist"));
        return categoryMapper.toCategoryDto(category);
    }
}
