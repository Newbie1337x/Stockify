package org.stockify.model.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.stockify.model.dto.request.CategoryRequest;
import org.stockify.model.dto.response.CategoryResponse;
import org.stockify.model.entity.CategoryEntity;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.CategoryMapper;
import org.stockify.model.repository.CategoryRepository;
import org.stockify.model.exception.DuplicatedUniqueConstraintException;
import org.stockify.util.StringUtils;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }


    public List<CategoryResponse> findAll() {
        List<CategoryEntity> categories = categoryRepository.findAll();

        if (categories.isEmpty()) {
            logger.warn("category list is empty");
        }

        return categoryMapper.toResponseList(categories);
    }

    public CategoryResponse findById(int id) throws NotFoundException {
        CategoryEntity category = categoryRepository
                .findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Category with ID " + id + " not found"));

        return categoryMapper.toResponse(category);
    }

    public CategoryResponse save(CategoryRequest categoryRequestDTO) throws DuplicatedUniqueConstraintException {

        String name = StringUtils.capitalizeFirst(categoryRequestDTO.name());

        if(categoryRepository.existsByName(name)){
            throw new DuplicatedUniqueConstraintException("Category with name " + name + " already exists");
        }

        return categoryMapper
                .toResponse
                        (categoryRepository.save(categoryMapper.toEntity(categoryRequestDTO)));

    }

    public void deleteById(int id) {
        categoryRepository.deleteById(id);
    }

    public void update(int id, CategoryRequest categoryRequestDTO)
            throws NotFoundException, DuplicatedUniqueConstraintException {

        categoryRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Category with ID " + id + " not found"));

        String name = StringUtils.capitalizeFirst(categoryRequestDTO.name());

        if(categoryRepository.existsByName(name)){
            throw new DuplicatedUniqueConstraintException("Category with name " + name + " already exists");
        }

        categoryRepository.save(categoryMapper.toEntity(categoryRequestDTO));
    }

    public void patch(int id, CategoryRequest categoryRequestDTO) throws NotFoundException, DuplicatedUniqueConstraintException {

        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with ID " + id + " not found"));

        String name = StringUtils.capitalizeFirst(categoryRequestDTO.name());

        if(categoryRepository.existsByName(categoryRequestDTO.name())){
            throw new DuplicatedUniqueConstraintException("Category with name " + name + " already exists");
        }

        if(categoryRequestDTO.name() != null){
            category.setName(categoryRequestDTO.name());
        }
        categoryRepository.save(category);
    }

}