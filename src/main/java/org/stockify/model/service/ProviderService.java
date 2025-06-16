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

/**
 * Service class for managing providers in the system.
 * It includes operations
 * for saving, retrieving, updating, and deleting providers, as well as handling
 * the association between products and providers.
 */
@Service
@RequiredArgsConstructor
public class ProviderService {

    private final ProviderRepository providerRepository;
    private final ProductRepository productRepository;
    private final ProviderMapper providerMapper;

    /**
     * Saves a new provider in the system.
     *
     * @param providerRequest DTO containing the provider's data
     * @return DTO with the details of the created provider
     */
    public ProviderResponse save(ProviderRequest providerRequest) {
        return providerMapper
                .toResponseDTO(providerRepository.save(providerMapper.toEntity(providerRequest)));
    }

    /**
     * Saves multiple providers in the system in a single operation.
     *
     * @param providers List of DTOs with provider data to create
     * @return Response containing the created providers and any errors
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
     * Retrieves all providers applying filters and pagination.
     *
     * @param pageable Pagination information
     * @param filterRequest DTO with the filters to apply (name, business name, tax ID, etc.)
     * @return A page of providers that match the filters
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
     * Finds a provider by its ID.
     *
     * @param id ID of the provider to search for
     * @return DTO with the details of the found provider
     * @throws NotFoundException if no provider is found with the specified ID
     */
    public ProviderResponse findById(long id) {
        ProviderEntity provider = getProviderById(id);
        return providerMapper.toResponseDTO(provider);
    }

    /**
     * Logically deletes a provider (marks it as inactive).
     *
     * @param id ID of the provider to delete logically
     * @return DTO with the updated provider details
     * @throws NotFoundException if no provider is found with the specified ID
     */
    public ProviderResponse logicalDelete(Long id) {
        ProviderEntity provider = getProviderById(id);
        provider.setActive(false);
        return providerMapper.toResponseDTO(providerRepository.save(provider));
    }

    /**
     * Finds providers associated with a product with pagination.
     *
     * @param productID ID of the product whose providers are being searched for
     * @param pageable Pagination information
     * @return A page of providers associated with the product
     */
    public Page<ProviderResponse> findAllProvidersByProductID(Long productID, Pageable pageable) {
        return providerRepository
                .findAllByProductList_Id(pageable, productID)
                .map(providerMapper::toResponseDTO);
    }

    /**
     * Assigns a product to a provider.
     *
     * @param providerID ID of the provider to assign the product to
     * @param productID ID of the product to assign to the provider
     * @return DTO with the updated provider details
     * @throws NotFoundException if the provider or the product with the specified IDs cannot be found
     */
    public ProviderResponse assignProductToProvider(Long providerID, Long productID) {
        ProviderEntity provider = getProviderById(providerID);
        ProductEntity product = getProductById(productID);
        provider.getProductList().add(product);
        product.getProviders().add(provider);

        providerRepository.save(provider);
        productRepository.save(product);

        return providerMapper.toResponseDTO(provider);
    }

    /**
     * Unassigns a product from a provider.
     *
     * @param providerID ID of the provider from which the product will be unassigned
     * @param productID ID of the product to unassign from the provider
     * @return DTO with the updated provider details
     * @throws NotFoundException if the provider or the product with the specified IDs cannot be found
     */
    public ProviderResponse unassignProductToProvider(Long providerID, Long productID) {
        ProviderEntity provider = getProviderById(providerID);
        ProductEntity product = getProductById(productID);
        provider.getProductList().remove(product);
        product.getProviders().remove(provider);

        providerRepository.save(provider);
        productRepository.save(product);

        return providerMapper.toResponseDTO(provider);
    }

    // Auxiliaries

    /**
     * Auxiliary method to find a product entity by its ID.
     *
     * @param id ID of the product to find
     * @return The found product entity
     * @throws NotFoundException if no product with the specified ID is found
     */
    private ProductEntity getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product with ID " + id + " not found"));
    }

    /**
     * Auxiliary method to find a provider entity by its ID.
     *
     * @param id ID of the provider to find
     * @return The found provider entity
     * @throws NotFoundException if no provider with the specified ID is found
     */
    private ProviderEntity getProviderById(Long id) {
        return providerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Provider with ID " + id + " not found"));
    }
}
