package org.stockify.model.service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.ProviderFilterRequest;
import org.stockify.dto.request.ProviderRequest;
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
public class ProviderService {

    private final ProviderRepository providerRepository;
    private final ProductRepository productRepository;
    private final ProviderMapper providerMapper;

    public ProviderService(ProviderRepository providerRepository, ProductRepository productRepository, ProviderMapper providerMapper) {
        this.providerRepository = providerRepository;
        this.productRepository = productRepository;
        this.providerMapper = providerMapper;
    }

    //---Crud operations---

    public ProviderResponse save(ProviderRequest providerRequest) {
        return providerMapper
                .toResponseDTO(providerRepository.save
                        (providerMapper.toEntity(providerRequest)));
    }

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

    public Page<ProviderResponse> findAll(Pageable pageable, ProviderFilterRequest filterRequest) {
        // Handle the hide parameter
        Boolean hide = filterRequest.getHide();

        // If hide is null (default), show only active providers
        // If hide is true, show all providers (active and inactive)
        // If hide is false, show only inactive providers
        if (hide == null) {
            filterRequest.setActive(true);
        } else if (hide) {
            filterRequest.setActive(null); // Show all providers
        } else {
            filterRequest.setActive(false); // Show only inactive providers
        }

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


    public ProviderResponse findById(long id) {
        ProviderEntity provider = getProviderById(id);
        return providerMapper.toResponseDTO(provider);
    }


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

    public Page<ProviderResponse> findAllProvidersByProductID(Long productID, Pageable pageable) {
        return providerRepository
                .findAllByProductList_Id(pageable, productID)
                .map(providerMapper::toResponseDTO);
    }

    public ProviderResponse assignProductToProvider(Long providerID, Long productID){
        ProviderEntity provider = getProviderById(providerID);
        ProductEntity product = getProductById(productID);
        provider.getProductList().add(product);
        product.getProviders().add(provider);

        providerRepository.save(provider);
        productRepository.save(product);

        return providerMapper.toResponseDTO(provider);
    }

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
    private ProductEntity getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product with ID " + id + " not found"));
    }


    private ProviderEntity getProviderById(Long id) {
        return providerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Provider with ID " + id + " not found"));
    }
}
