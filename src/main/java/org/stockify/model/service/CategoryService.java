package org.stockify.model.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.CategoryRequest;
import org.stockify.dto.response.CategoryResponse;
import org.stockify.model.entity.CategoryEntity;
import org.stockify.model.exception.DuplicatedUniqueConstraintException;
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
        findByNameIgnoreCase(request.getName());
        CategoryEntity entity = categoryMapper.toEntity(request);
        return categoryMapper.toResponse(categoryRepository.save(entity));
    }

    public void deleteById(int id) {
        categoryRepository.deleteById(id);
    }

    public CategoryResponse update(int id, CategoryRequest request) {
        CategoryEntity existingCategory = findEntityById(id);

        if (!existingCategory.getName().equalsIgnoreCase(request.getName())) {
            findByNameIgnoreCase(request.getName());
        }

        CategoryEntity updatedEntity = categoryMapper.updateEntityFromRequest(request, existingCategory);
        return categoryMapper.toResponse(categoryRepository.save(updatedEntity));
    }
    public void patch(int id, CategoryRequest request) {
        CategoryEntity existingCategory = findEntityById(id);

        if (request.getName() != null && !existingCategory.getName().equalsIgnoreCase(request.getName())) {
            findByNameIgnoreCase(request.getName());
        }


        categoryMapper.patchEntityFromRequest(request, existingCategory);
        categoryRepository.save(existingCategory);
    }
    
    private CategoryEntity findEntityById(int id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with ID " + id + " not found"));
    }

    private void findByNameIgnoreCase(String name){
      if (categoryRepository.existsByNameIgnoreCase(name)){
          throw new DuplicatedUniqueConstraintException("Category with name " + name + " already exists");
      }
    }

    }