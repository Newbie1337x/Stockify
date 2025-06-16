package org.stockify.model.service;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Service
@RequiredArgsConstructor

public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    /**
     * Busca categorías aplicando filtros y paginación.
     * 
     * @param pageable Información de paginación
     * @param name Nombre de la categoría para filtrar (opcional)
     * @return Página de categorías que cumplen con los filtros
     */
    public Page<CategoryResponse> findAll(Pageable pageable, String name) {
        Specification<CategoryEntity> spec = CategorySpecification.hasName(name);
        Page<CategoryEntity> page = categoryRepository.findAll(spec, pageable);
        return page.map(categoryMapper::toResponse);
    }

    /**
     * Busca una categoría por su ID.
     * 
     * @param id ID de la categoría a buscar
     * @return DTO con los datos de la categoría encontrada
     * @throws NotFoundException si no se encuentra ninguna categoría con el ID especificado
     */
    public CategoryResponse findById(int id) {
        return categoryMapper.toResponse(findEntityById(id));
    }

    /**
     * Guarda una nueva categoría en el sistema.
     * 
     * @param request DTO con los datos de la categoría a crear
     * @return DTO con los datos de la categoría creada
     */
    public CategoryResponse save(CategoryRequest request) {
        CategoryEntity entity = categoryMapper.toEntity(request);
        return categoryMapper.toResponse(categoryRepository.save(entity));
    }

    /**
     * Elimina una categoría por su ID.
     * 
     * @param id ID de la categoría a eliminar
     */
    public void deleteById(int id) {
        categoryRepository.deleteById(id);
    }

    /**
     * Actualiza completamente una categoría existente.
     * 
     * @param id ID de la categoría a actualizar
     * @param request DTO con los nuevos datos de la categoría
     * @return DTO con los datos de la categoría actualizada
     * @throws NotFoundException si no se encuentra ninguna categoría con el ID especificado
     */
    public CategoryResponse update(int id, CategoryRequest request) {
        CategoryEntity existingCategory = findEntityById(id);
        CategoryEntity updatedEntity = categoryMapper.updateEntityFromRequest(request, existingCategory);
        return categoryMapper.toResponse(categoryRepository.save(updatedEntity));
    }

    /**
     * Actualiza parcialmente una categoría existente.
     * 
     * @param id ID de la categoría a actualizar parcialmente
     * @param request DTO con los datos a actualizar de la categoría
     * @return DTO con los datos de la categoría actualizada
     * @throws NotFoundException si no se encuentra ninguna categoría con el ID especificado
     */
    public CategoryResponse patch(int id, CategoryRequest request) {
        CategoryEntity existingCategory = findEntityById(id);
        categoryMapper.patchEntityFromRequest(request, existingCategory);
       return categoryMapper.toResponse(categoryRepository.save(existingCategory));
    }

    /**
     * Metodo auxiliar para buscar una entidad de categoría por su ID.
     * 
     * @param id ID de la categoría a buscar
     * @return La entidad de la categoría encontrada
     * @throws NotFoundException si no se encuentra ninguna categoría con el ID especificado
     */
    private CategoryEntity findEntityById(int id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with ID " + id + " not found"));
    }


    }
