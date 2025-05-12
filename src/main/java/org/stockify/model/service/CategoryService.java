package org.stockify.model.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.stockify.model.dto.request.CategoryRequest;
import org.stockify.model.dto.response.CategoryResponse;
import org.stockify.model.entity.CategoryEntity;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.CategoryMapper;
import org.stockify.model.repository.CategoryRepository;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public Page<CategoryResponse> findAll(Pageable pageable) {
        Page<CategoryEntity> page = categoryRepository.findAll(pageable);

        if (page.isEmpty()) {
            logger.warn("category list is empty for pageable: {}", pageable);
        }

        return page.map(categoryMapper::toResponse);
    }
    public CategoryResponse findById(int id) {
        return categoryMapper.toResponse(findEntityById(id));
    }

    public CategoryResponse save(CategoryRequest request) {
        CategoryEntity entity = categoryMapper.toEntity(request);
        return categoryMapper.toResponse(categoryRepository.save(entity));
    }

    public void deleteById(int id) {
        categoryRepository.deleteById(id);
    }

    public CategoryResponse update(int id, CategoryRequest request) {
        return categoryMapper.toResponse(
                categoryRepository.save
                        (categoryMapper
                                .updateEntityFromRequest
                                        (request,findEntityById(id))));
    }
    public void patch(int id, CategoryRequest request) {
        CategoryEntity category = findEntityById(id);
        categoryMapper.patchEntityFromRequest(request, category);
        categoryRepository.save(category);
    }
    
    private CategoryEntity findEntityById(int id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with ID " + id + " not found"));
    }
    }