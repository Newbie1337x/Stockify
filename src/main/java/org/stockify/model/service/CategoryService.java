package org.stockify.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.category.CategoryRequest;
import org.stockify.dto.response.CategoryResponse;
import org.stockify.model.entity.CategoryEntity;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.CategoryMapper;
import org.stockify.model.repository.CategoryRepository;
import org.stockify.model.specification.CategorySpecification;

/**
 * Service class responsible for managing categories, including
 * operations such as searching, creating, updating, deleting,
 * and partial updates of categories.
 */
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    /**
     * Retrieves a paginated list of categories filtered optionally by name.
     *
     * @param pageable pagination information
     * @param name     optional category name filter
     * @return a paginated list of categories matching the filter criteria
     */
    public Page<CategoryResponse> findAll(Pageable pageable, String name) {
        Specification<CategoryEntity> spec = CategorySpecification.hasName(name);
        Page<CategoryEntity> page = categoryRepository.findAll(spec, pageable);
        return page.map(categoryMapper::toResponse);
    }

    /**
     * Finds a category by its ID.
     *
     * @param id the ID of the category to find
     * @return a DTO with the category data
     * @throws NotFoundException if no category is found with the specified ID
     */
    public CategoryResponse findById(int id) {
        return categoryMapper.toResponse(findEntityById(id));
    }

    /**
     * Saves a new category in the system.
     *
     * @param request DTO containing the data for the new category
     * @return a DTO with the saved category data
     */
    public CategoryResponse save(CategoryRequest request) {
        CategoryEntity entity = categoryMapper.toEntity(request);
        return categoryMapper.toResponse(categoryRepository.save(entity));
    }

    /**
     * Deletes a category by its ID.
     *
     * @param id the ID of the category to delete
     */
    public void deleteById(int id) {
        categoryRepository.deleteById(id);
    }

    /**
     * Fully updates an existing category with the provided data.
     *
     * @param id      the ID of the category to update
     * @param request DTO containing the new category data
     * @return a DTO with the updated category data
     * @throws NotFoundException if no category is found with the specified ID
     */
    public CategoryResponse update(int id, CategoryRequest request) {
        CategoryEntity existingCategory = findEntityById(id);
        CategoryEntity updatedEntity = categoryMapper.updateEntityFromRequest(request, existingCategory);
        return categoryMapper.toResponse(categoryRepository.save(updatedEntity));
    }

    /**
     * Partially updates an existing category with the provided data.
     *
     * @param id      the ID of the category to partially update
     * @param request DTO containing the fields to update
     * @return a DTO with the updated category data
     * @throws NotFoundException if no category is found with the specified ID
     */
    public CategoryResponse patch(int id, CategoryRequest request) {
        CategoryEntity existingCategory = findEntityById(id);
        categoryMapper.patchEntityFromRequest(request, existingCategory);
        return categoryMapper.toResponse(categoryRepository.save(existingCategory));
    }

    /**
     * Helper method to find a CategoryEntity by its ID.
     *
     * @param id the ID of the category to find
     * @return the found CategoryEntity
     * @throws NotFoundException if no category is found with the specified ID
     */
    private CategoryEntity findEntityById(int id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with ID " + id + " not found"));
    }
}
