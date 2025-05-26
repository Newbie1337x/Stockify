package org.stockify.model.service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.ProviderFilterRequest;
import org.stockify.dto.request.ProviderRequest;
import org.stockify.dto.response.BulkProviderResponse;
import org.stockify.dto.response.ProviderResponse;
import org.stockify.model.entity.CategoryEntity;
import org.stockify.model.entity.ProductEntity;
import org.stockify.model.entity.ProviderEntity;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.ProviderMapper;
import org.stockify.model.repository.CategoryRepository;
import org.stockify.model.repository.ProductRepository;
import org.stockify.model.repository.ProviderRepository;
import org.stockify.model.specification.ProviderSpecification;

import java.util.List;


@Service
public class ProviderService {

    private final ProviderRepository providerRepository;
    private final ProductRepository productRepository;
    private final ProviderMapper providerMapper;
    private final CategoryRepository categoryRepository;

    public ProviderService(ProviderRepository providerRepository, ProductRepository productRepository, ProviderMapper providerMapper, CategoryRepository categoryRepository) {
        this.providerRepository = providerRepository;
        this.productRepository = productRepository;
        this.providerMapper = providerMapper;
        this.categoryRepository = categoryRepository;
    }

    //---Crud operations---

    public ProviderResponse save(ProviderRequest providerRequest) {
        return providerMapper
                .toResponseDTO(providerRepository.save
                        (providerMapper.toEntity(providerRequest)));
    }

    public BulkProviderResponse saveAll(List<ProviderRequest> providers) {
        List<String> errors = providers.stream()
                .map(providerMapper::toEntity)
                .map(providerRepository::save)
                .filter(p -> !p.isActive())
                .map(p -> "Error creating provider with ID: " + p.getId())
                .toList();

        List<ProviderResponse> responses = providers.stream()
                .map(providerMapper::toEntity)
                .map(providerRepository::save)
                .filter(ProviderEntity::isActive)
                .map(providerMapper::toResponseDTO)
                .toList();
        return new BulkProviderResponse(responses, errors);
    }

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


    public ProviderResponse findById(long id) {
        ProviderEntity provider = getProviderById(id);
        return providerMapper.toResponseDTO(provider);
    }

    /*

    public Page<ProviderResponse> findByName(Pageable pageable, String name) {
        Page<ProviderEntity> page = providerRepository.findByName(pageable, name);
        return page.map(providerMapper::toResponseDTO);
    }

    public ProviderResponse findByBusinessName(String businessName) {
        return providerMapper.toResponseDTO(providerRepository.findByBusinessName(businessName));
    }

    public ProviderResponse findByTaxId(String taxId) {
        return providerMapper.toResponseDTO(providerRepository.findByTaxId(taxId));
    }

    */

    public ProviderResponse logicalDelete(Long id) {
        ProviderEntity provider = getProviderById(id);
        provider.setActive(false);
        return providerMapper.toResponseDTO(providerRepository.save(provider));
    }

    public ProviderResponse delete(Long id) {
        ProviderResponse provider = providerMapper.toResponseDTO(getProviderById(id));
        providerRepository.deleteById(id);
        return provider;
    }

    //Product Logic

    public Page<ProviderResponse> findAllProvidersByProductID(int productID, Pageable pageable) {
        return providerRepository
                .findAllByProductList_Id(pageable, productID)
                .map(providerMapper::toResponseDTO);
    }

    public ProviderResponse assignProductToProvider(long providerID, int productID){
        ProviderEntity provider = getProviderById(providerID);
        ProductEntity product = getProductById(productID);
        provider.getProductList().add(product);
        return providerMapper.toResponseDTO(provider);
    }

    public ProviderResponse unassignProductToProvider(Long providerID,int productID){
        ProviderEntity provider = getProviderById(providerID);
        ProductEntity product = getProductById(productID);
        provider.getProductList().remove(product);
        return providerMapper.toResponseDTO(provider);
    }


    //Auxiliar
    private ProductEntity getProductById(int id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product with ID " + id + " not found"));
    }

    private CategoryEntity getCategoryById(int id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with ID " + id + " not found"));
    }

    private ProviderEntity getProviderById(Long id) {
        return providerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Provider with ID " + id + " not found"));
    }
}

    /* TODO ---Gestión de órdenes de compra

            crearOrdenDeCompra(proveedorId, datosOrden)

            listarOrdenesDeCompra(proveedorId, ordenId)

            obtenerOrdenDeCompra(ordenId)

            actualizarEstadoOrden(ordenId, estado)

      TODO ---Historial

            obtenerHistorialDeCompras(proveedorId)

            calcularTotalCompradoAProveedor(proveedorId, periodo)
    */

