package org.stockify.model.service;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.stockify.dto.request.product.ProductCSVRequest;
import org.stockify.dto.request.product.ProductFilterRequest;
import org.stockify.dto.request.product.ProductRequest;
import org.stockify.dto.response.BulkItemResponse;
import org.stockify.dto.response.BulkProductResponse;
import org.stockify.dto.response.CategoryResponse;
import org.stockify.dto.response.ProductResponse;
import org.stockify.model.entity.CategoryEntity;
import org.stockify.model.entity.ProductEntity;
import org.stockify.model.entity.ProviderEntity;
import org.stockify.model.exception.DuplicatedUniqueConstraintException;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.CategoryMapper;
import org.stockify.model.mapper.ProductMapper;
import org.stockify.model.repository.CategoryRepository;
import org.stockify.model.repository.ProductRepository;
import org.stockify.model.repository.ProviderRepository;
import org.stockify.model.specification.ProductSpecifications;
import org.stockify.model.specification.SpecificationBuilder;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor

public class ProductService {
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final CategoryMapper categoryMapper;
    private final ProviderRepository providerRepository;

    /**
     * Busca un producto por su ID.
     * 
     * @param id ID del producto a buscar
     * @return DTO con los datos del producto encontrado
     * @throws NotFoundException si no se encuentra ningún producto con el ID especificado
     */
    public ProductResponse findById(Long id) {
        return productMapper.toResponse(getProductById(id));
    }

    /**
     * Busca productos aplicando filtros y paginación.
     * 
     * @param pageable Información de paginación
     * @param filterRequest DTO con los filtros a aplicar (precio, nombre, descripción, etc.)
     * @return Página de productos que cumplen con los filtros
     */
    public Page<ProductResponse> findAll(Pageable pageable, ProductFilterRequest filterRequest) {
        Specification<ProductEntity> spec = new SpecificationBuilder<ProductEntity>()
                .add(filterRequest.getPrice() != null ? ProductSpecifications.byPrice(filterRequest.getPrice()) : null)
                .add(filterRequest.getName() != null && !filterRequest.getName().isEmpty() ? ProductSpecifications.byName(filterRequest.getName()) : null)
                .add(filterRequest.getDescription() != null && !filterRequest.getDescription().isEmpty() ? ProductSpecifications.byDescription(filterRequest.getDescription()) : null)
                .add(filterRequest.getBarcode() != null && !filterRequest.getBarcode().isEmpty() ? ProductSpecifications.byBarCode(filterRequest.getBarcode()) : null)
                .add(filterRequest.getSku() != null && !filterRequest.getSku().isEmpty() ? ProductSpecifications.bySku(filterRequest.getSku()) : null)
                .add(filterRequest.getBrand() != null && !filterRequest.getBrand().isEmpty() ? ProductSpecifications.byBrand(filterRequest.getBrand()) : null)
                .add(filterRequest.getCategory() != null && !filterRequest.getCategory().isEmpty() ? ProductSpecifications.byCategory(filterRequest.getCategory()) : null)
                .add(filterRequest.getProvider() != null && !filterRequest.getProvider().isEmpty() ? ProductSpecifications.byProvider(filterRequest.getProvider()) : null)
                .add(filterRequest.getProviders() != null && !filterRequest.getProviders().isEmpty() ? ProductSpecifications.byProviders(filterRequest.getProviders()) : null)
                .add(filterRequest.getCategories() != null && !filterRequest.getCategories().isEmpty() ? ProductSpecifications.byCategories(filterRequest.getCategories()) : null)
                .add(filterRequest.getPriceGreater() != null ? ProductSpecifications.byPriceGreaterThan(filterRequest.getPriceGreater()) : null)
                .add(filterRequest.getPriceLess() != null ? ProductSpecifications.byPriceLessThan(filterRequest.getPriceLess()) : null)
                .add(filterRequest.getPriceBetween() != null && filterRequest.getPriceBetween().size() == 2
                        ? ProductSpecifications.byPriceBetween(
                        filterRequest.getPriceBetween().get(0),
                        filterRequest.getPriceBetween().get(1))
                        : null)
                .add(filterRequest.getStock() != null ? ProductSpecifications.byStock(filterRequest.getStock()) : null)
                .add(filterRequest.getStockLessThan() != null ? ProductSpecifications.byStockLessThan(filterRequest.getStockLessThan()) : null)
                .add(filterRequest.getStockGreaterThan() != null ? ProductSpecifications.byStockGreaterThan(filterRequest.getStockGreaterThan()) : null)
                .add(filterRequest.getStockBetween() != null && filterRequest.getStockBetween().size() == 2
                        ? ProductSpecifications.byStockBetween(
                        filterRequest.getStockBetween().get(0),
                        filterRequest.getStockBetween().get(1))
                        : null)
                .build();

        Page<ProductEntity> page = productRepository.findAll(spec, pageable);

        if (page.isEmpty()) {
            logger.warn("Products list is empty for pageable: {}", pageable);
        }

        return page.map(productMapper::toResponse);
    }



    /**
     * Guarda un nuevo producto en el sistema.
     * Si se especifican categorías que no existen, se crean automáticamente.
     * 
     * @param request DTO con los datos del producto a crear
     * @return DTO con los datos del producto creado
     * @throws DuplicatedUniqueConstraintException si ya existe un producto con el mismo código de barras o SKU
     */
    public ProductResponse save(ProductRequest request) throws DuplicatedUniqueConstraintException {
        ProductEntity product = productMapper.toEntity(request);

        for (String categoryName : request.categories()) {
            CategoryEntity category = categoryRepository.findByName(categoryName)
                    .orElseGet(() -> {
                        CategoryEntity newCategory = new CategoryEntity();
                        newCategory.setName(categoryName);
                        return categoryRepository.save(newCategory);
                    });

            product.getCategories().add(category);
        }

        product = productRepository.save(product);
        return productMapper.toResponse(product);
    }

    /**
     * Guarda múltiples productos en el sistema en una sola operación.
     * 
     * @param requests Lista de DTOs con los datos de los productos a crear
     * @return Respuesta con estadísticas de la operación (creados, omitidos, errores) y detalles de cada elemento
     */
    public BulkProductResponse saveAll(List<ProductRequest> requests) {
        List<BulkItemResponse> results = new ArrayList<>();
        int created = 0, skipped = 0, error = 0;

        for (ProductRequest req : requests) {
            try {
                save(req);
                created++;
                results.add(new BulkItemResponse(req.name(), "CREATED", null));
            } catch (DataIntegrityViolationException ex) {
                skipped++;
                results.add(new BulkItemResponse(
                        req.name(),
                        "SKIPPED",
                        "Duplicated, ítem skipped"
                ));
            } catch (DuplicatedUniqueConstraintException ex) {
                skipped++;
                results.add(new BulkItemResponse(
                        req.name(),
                        "SKIPPED",
                        ex.getMessage()
                ));
            } catch (IllegalArgumentException | IllegalStateException ex) {
                error++;
                results.add(new BulkItemResponse(
                        req.name(),
                        "ERROR",
                        "Invalid data: " + ex.getMessage()
                ));
            } catch (Exception ex) {
                error++;
                logger.error("Unexpected error saving product {}: {}", req.name(), ex.getMessage(), ex);
                results.add(new BulkItemResponse(
                        req.name(),
                        "ERROR",
                        "Unexpected error: " + ex.getMessage()
                ));
            }
        }
        return new BulkProductResponse(requests.size(),created,skipped,error,results);
    }


    /**
     * Importa productos desde un archivo CSV.
     *
     * @param archivo Archivo CSV con los datos de los productos
     * @return Respuesta con estadísticas de la operación (creados, omitidos, errores) y detalles de cada elemento
     * @throws Exception si ocurre un error al procesar el archivo
     */
    public BulkProductResponse importProductsCsv(MultipartFile archivo) throws Exception {
        // Parsear CSV a DTOs
        InputStreamReader reader = new InputStreamReader(archivo.getInputStream());

        List<ProductCSVRequest> csvDtos = new CsvToBeanBuilder<ProductCSVRequest>(reader)
                .withType(ProductCSVRequest.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build()
                .parse();

        // Convertir DTOs a ProductRequest
        List<ProductRequest> requests = csvDtos.stream()
                .map(productMapper::toRequest)
                .collect(Collectors.toList());

        return saveAll(requests);
    }

    /**
     * Elimina un producto por su ID.
     * 
     * @param id ID del producto a eliminar
     */
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    /**
     * Actualiza completamente un producto existente.
     * 
     * @param id ID del producto a actualizar
     * @param request DTO con los nuevos datos del producto
     * @return DTO con los datos del producto actualizado
     * @throws NotFoundException si no se encuentra ningún producto con el ID especificado
     */
    public ProductResponse update(Long id, ProductRequest request) {
        ProductEntity product = getProductById(id);
        productMapper.updateEntityFromRequest(request, product);
        return productMapper.toResponse(productRepository.save(product));
    }

    /**
     * Actualiza parcialmente un producto existente.
     * 
     * @param id ID del producto a actualizar parcialmente
     * @param request DTO con los datos a actualizar del producto
     * @return DTO con los datos del producto actualizado
     * @throws NotFoundException si no se encuentra ningún producto con el ID especificado
     */
    public ProductResponse patch(Long id, ProductRequest request) {
        ProductEntity product = getProductById(id);
        productMapper.patchEntityFromRequest(request,product);
        return productMapper.toResponse(productRepository.save(product));
    }


    //Categories Logic

    /**
     * Elimina una categoría de un producto.
     * 
     * @param categoryId ID de la categoría a eliminar del producto
     * @param productId ID del producto del que se eliminará la categoría
     * @return DTO con los datos del producto actualizado
     * @throws NotFoundException si no se encuentra el producto o la categoría con los IDs especificados
     */
    public ProductResponse deleteCategoryFromProduct(int categoryId, Long productId) {
        ProductEntity product = getProductById(productId);
        CategoryEntity category = getCategoryById(categoryId);
        product.getCategories().remove(category);
        return productMapper.toResponse(productRepository.save(product));
    }

    /**
     * Elimina todas las categorías de un producto.
     * 
     * @param productId ID del producto del que se eliminarán todas las categorías
     * @return DTO con los datos del producto actualizado
     * @throws NotFoundException si no se encuentra el producto con el ID especificado
     */
    public ProductResponse deleteAllCategoryFromProduct(Long productId) {
        ProductEntity product = getProductById(productId);
        product.getCategories().clear();
        return productMapper.toResponse(productRepository.save(product));
    }

    /**
     * Busca las categorías asociadas a un producto con paginación.
     * 
     * @param productId ID del producto del que se buscarán las categorías
     * @param pageable Informacion de paginación
     * @return Pagina de categorias asociadas al producto
     * @throws NotFoundException si no se encuentra el producto con el ID especificado
     */
    public Page<CategoryResponse> findCategoriesByProductId(Long productId, Pageable pageable) {
        if (productRepository.findById(productId).isEmpty()) {
            throw new NotFoundException("Product with ID " + productId + " not found");
        }

        Page<CategoryEntity> categoryPage = productRepository.findCategoriesByProductId(productId, pageable);

        if (categoryPage.isEmpty()) {
            logger.info("No categories found for product ID: {}", productId);
        }

        return categoryPage.map(categoryMapper::toResponse);
    }

    /**
     * Agrega una categoría a un producto.
     * 
     * @param categoryId ID de la categoría a agregar al producto
     * @param productId ID del producto al que se agregará la categoría
     * @return DTO con los datos del producto actualizado
     * @throws NotFoundException si no se encuentra el producto o la categoría con los IDs especificados
     */
    public ProductResponse addCategoryToProduct(int categoryId, Long productId) {
        ProductEntity product = getProductById(productId);
        CategoryEntity category = getCategoryById(categoryId);
        product.getCategories().add(category);
        return productMapper.toResponse(productRepository.save(product));
    }

    //Provider logic

    /**
     * Asigna un proveedor a un producto.
     * 
     * @param productID ID del producto al que se asignará el proveedor
     * @param providerID ID del proveedor a asignar al producto
     * @return DTO con los datos del producto actualizado
     * @throws NotFoundException si no se encuentra el producto o el proveedor con los IDs especificados
     * @throws ResponseStatusException si el proveedor ya está asignado al producto
     */
    public ProductResponse assignProviderToProduct(Long productID, Long providerID){
        ProductEntity product = getProductById(productID);
        ProviderEntity provider = getProviderById(providerID);
        product.getProviders().add(provider);
        provider.getProductList().add(product);

        try{
            productRepository.save(product);
            providerRepository.save(provider);

        }catch (DataIntegrityViolationException ex){
            logger.error("Error saving product {} and provider {}: {}", productID, providerID, ex.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Provider "+ providerID+ " already assigned to product "+ productID + " or the other way around");
        }


        return productMapper.toResponse(product);
    }

    /**
     * Desasigna un proveedor de un producto.
     * 
     * @param productID ID del producto del que se desasignará el proveedor
     * @param providerID ID del proveedor a desasignar del producto
     * @return DTO con los datos del producto actualizado
     * @throws NotFoundException si no se encuentra el producto o el proveedor con los IDs especificados
     */
    public ProductResponse unassignProviderFromProduct(Long productID, Long providerID){
        ProductEntity product = getProductById(productID);
        ProviderEntity provider = getProviderById(providerID);

        product.getProviders().remove(provider);
        provider.getProductList().remove(product);

        productRepository.save(product);
        providerRepository.save(provider);

        return productMapper.toResponse(product);
    }

    /**
     * Busca productos asociados a un proveedor con paginación.
     * 
     * @param providerID ID del proveedor del que se buscarán los productos
     * @param pageable Informacion de paginación
     * @return Página de productos asociados al proveedor
     */
    public Page<ProductResponse> findProductsByProviderId(Long providerID, Pageable pageable) {

        Page<ProductResponse> page = productRepository
                .findAllByProviders_Id(providerID, pageable)
                .map(productMapper::toResponse);

        if (page.isEmpty()) {
            logger.warn("No products found for provider ID: {}", providerID);
        }

        return page;
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
     * Meodo auxiliar para buscar una entidad de categoría por su ID.
     * 
     * @param id ID de la categoría a buscar
     * @return La entidad de la categoría encontrada
     * @throws NotFoundException si no se encuentra ninguna categoría con el ID especificado
     */
    private CategoryEntity getCategoryById(int id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with ID " + id + " not found"));
    }

    /**
     * Metodo auxiliar para buscar una entidad de proveedor por su ID.
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
