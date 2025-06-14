package org.stockify.model.service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.provider.ProviderFilterRequest;
import org.stockify.dto.request.provider.ProviderRequest;
import org.stockify.dto.response.BulkProviderResponse;
import org.stockify.dto.response.ProviderResponse;
import org.stockify.model.entity.ProductEntity;
import org.stockify.model.entity.ProviderEntity;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.ProviderMapper;
import org.stockify.model.repository.ProductRepository;
import org.stockify.model.repository.ProviderRepository;
import org.stockify.model.specification.ProviderSpecification;

import java.util.List;


@Service
@RequiredArgsConstructor

public class ProviderService {

    private final ProviderRepository providerRepository;
    private final ProductRepository productRepository;
    private final ProviderMapper providerMapper;

    //---Crud operations---

    /**
     * Guarda un nuevo proveedor en el sistema.
     * 
     * @param providerRequest DTO con los datos del proveedor a crear
     * @return DTO con los datos del proveedor creado
     */
    public ProviderResponse save(ProviderRequest providerRequest) {
        return providerMapper
                .toResponseDTO(providerRepository.save
                        (providerMapper.toEntity(providerRequest)));
    }

    /**
     * Guarda múltiples proveedores en el sistema en una sola operación.
     * 
     * @param providers Lista de DTOs con los datos de los proveedores a crear
     * @return Respuesta con los proveedores creados y posibles errores
     */
    public BulkProviderResponse saveAll(List<ProviderRequest> providers) {
        List<String> errors = new java.util.ArrayList<>();
        List<ProviderResponse> responses = new java.util.ArrayList<>();

        for (ProviderRequest request : providers) {
            try {
                ProviderEntity entity = providerMapper.toEntity(request);
                ProviderEntity savedEntity = providerRepository.save(entity);

                if (savedEntity.isActive()) {
                    responses.add(providerMapper.toResponseDTO(savedEntity));
                } else {
                    errors.add("Error creating provider with ID: " + savedEntity.getId());
                }
            } catch (Exception e) {
                errors.add("Error creating provider: " + e.getMessage());
            }
        }

        return new BulkProviderResponse(responses, errors);
    }

    /**
     * Busca proveedores aplicando filtros y paginación.
     * 
     * @param pageable Información de paginación
     * @param filterRequest DTO con los filtros a aplicar (nombre, razón social, identificación fiscal, etc.)
     * @return Página de proveedores que cumplen con los filtros
     */
    public Page<ProviderResponse> findAll(Pageable pageable, ProviderFilterRequest filterRequest) {
        Specification<ProviderEntity> specification = Specification
                .where(ProviderSpecification.byName(filterRequest.getName()))
                .and(ProviderSpecification.byBusinessName(filterRequest.getBusinessName()))
                .and(ProviderSpecification.byTaxId(filterRequest.getTaxId()))
                .and(ProviderSpecification.byEmail(filterRequest.getEmail()))
                .and(ProviderSpecification.byPhone(filterRequest.getPhone()))
                .and(ProviderSpecification.byActive(filterRequest.getActive()))
                .and(ProviderSpecification.byId(filterRequest.getId()))
                .and(ProviderSpecification.byTaxAddress(filterRequest.getTaxAddress()));

        return providerRepository.findAll(specification, pageable)
                .map(providerMapper::toResponseDTO);
    }

    /**
     * Busca un proveedor por su ID.
     * 
     * @param id ID del proveedor a buscar
     * @return DTO con los datos del proveedor encontrado
     * @throws NotFoundException si no se encuentra ningún proveedor con el ID especificado
     */
    public ProviderResponse findById(long id) {
        ProviderEntity provider = getProviderById(id);
        return providerMapper.toResponseDTO(provider);
    }

    /**
     * Elimina logicamente un proveedor (lo marca como inactivo).
     * 
     * @param id ID del proveedor a eliminar lógicamente
     * @return DTO con los datos del proveedor actualizado
     * @throws NotFoundException si no se encuentra ningún proveedor con el ID especificado
     */
    public ProviderResponse logicalDelete(Long id) {
        ProviderEntity provider = getProviderById(id);
        provider.setActive(false);
        return providerMapper.toResponseDTO(providerRepository.save(provider));
    }

  /*  public ProviderResponse delete(Long id) {
        ProviderResponse provider = providerMapper.toResponseDTO(getProviderById(id));
        providerRepository.deleteById(id);
        return provider;
    }
*/
    //Product Logic

    /**
     * Busca proveedores asociados a un producto con paginación.
     * 
     * @param productID ID del producto del que se buscarán los proveedores
     * @param pageable Información de paginación
     * @return Página de proveedores asociados al producto
     */
    public Page<ProviderResponse> findAllProvidersByProductID(Long productID, Pageable pageable) {
        return providerRepository
                .findAllByProductList_Id(pageable, productID)
                .map(providerMapper::toResponseDTO);
    }

    /**
     * Asigna un producto a un proveedor.
     * 
     * @param providerID ID del proveedor al que se asignará el producto
     * @param productID ID del producto a asignar al proveedor
     * @return DTO con los datos del proveedor actualizado
     * @throws NotFoundException si no se encuentra el proveedor o el producto con los IDs especificados
     */
    public ProviderResponse assignProductToProvider(Long providerID, Long productID){
        ProviderEntity provider = getProviderById(providerID);
        ProductEntity product = getProductById(productID);
        provider.getProductList().add(product);
        product.getProviders().add(provider);

        providerRepository.save(provider);
        productRepository.save(product);

        return providerMapper.toResponseDTO(provider);
    }

    /**
     * Desasigna un producto de un proveedor.
     * 
     * @param providerID ID del proveedor del que se desasignará el producto
     * @param productID ID del producto a desasignar del proveedor
     * @return DTO con los datos del proveedor actualizado
     * @throws NotFoundException si no se encuentra el proveedor o el producto con los IDs especificados
     */
    public ProviderResponse unassignProductToProvider(Long providerID, Long productID){
        ProviderEntity provider = getProviderById(providerID);
        ProductEntity product = getProductById(productID);
        provider.getProductList().remove(product);
        product.getProviders().remove(provider);

        providerRepository.save(provider);
        productRepository.save(product);

        return providerMapper.toResponseDTO(provider);
    }

    //Auxiliar
    /**
     * Metodo auxiliar para buscar una entidad de producto por su ID.
     * 
     * @param id ID del producto a buscar
     * @return La entidad del producto encontrado
     * @throws NotFoundException si no se encuentra ningún producto con el ID especificado
     */
    private ProductEntity getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product with ID " + id + " not found"));
    }

    /**
     * Mtodo auxiliar para buscar una entidad de proveedor por su ID.
     * 
     * @param id ID del proveedor a buscar
     * @return La entidad del proveedor encontrado
     * @throws NotFoundException si no se encuentra ningún proveedor con el ID especificado
     */
    private ProviderEntity getProviderById(Long id) {
        return providerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Provider with ID " + id + " not found"));
    }
}
