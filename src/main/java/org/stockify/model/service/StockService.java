package org.stockify.model.service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.stockify.config.GlobalPreferencesConfig;
import org.stockify.dto.request.ProductFilterRequest;
import org.stockify.dto.request.stock.StockRequest;
import org.stockify.dto.request.stock.StockTransferRequest;
import org.stockify.dto.response.ProductStoreResponse;
import org.stockify.dto.response.StockResponse;
import org.stockify.model.entity.ProductEntity;
import org.stockify.model.entity.StockEntity;
import org.stockify.model.entity.StoreEntity;
import org.stockify.model.exception.InsufficientStockException;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.ProductStoreMapper;
import org.stockify.model.mapper.StockMapper;
import org.stockify.model.repository.ProductRepository;
import org.stockify.model.repository.StockRepository;
import org.stockify.model.repository.StoreRepository;
import org.stockify.model.specification.StockSpecifications;

import java.util.List;
@RequiredArgsConstructor

@Service
public class StockService {
    private final StockRepository stockRepository;
    private final ProductStoreMapper productStoreMapper;
    private final StockMapper stockMapper;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final GlobalPreferencesConfig globalPreferencesConfig;
    private final EmailService emailService;

    /**
     * Lista los productos disponibles en una tienda específica con filtros y paginación.
     * 
     * @param storeID ID de la tienda de la que se listarán los productos
     * @param pageable Información de paginación
     * @param filterRequest DTO con los filtros a aplicar (código de barras, SKU, nombre, etc.)
     * @return Página de productos con su stock en la tienda especificada
     */
    public Page<ProductStoreResponse> listProductsByStore (Long storeID, Pageable pageable, ProductFilterRequest filterRequest) {
        Specification<StockEntity> spec = Specification.where(StockSpecifications.byStoreId(storeID))
                .and(StockSpecifications.byProductBarCode(filterRequest.getBarcode()))
                .and(StockSpecifications.byProductSku(filterRequest.getSku()))
                .and(StockSpecifications.byProductName(filterRequest.getName()))
                .and(StockSpecifications.byProductBrand(filterRequest.getBrand()))
                .and(StockSpecifications.byProductDescription(filterRequest.getDescription()))
                .and(StockSpecifications.byProductPrice(filterRequest.getPrice()))
                .and(StockSpecifications.byProductPriceGreaterThan(filterRequest.getPriceGreater()))
                .and(StockSpecifications.byProductPriceLessThan(filterRequest.getPriceLess()))
                .and(StockSpecifications.byProductPriceBetween(
                        filterRequest.getPriceBetween() != null && filterRequest.getPriceBetween().size() == 2
                                ? filterRequest.getPriceBetween().get(0)
                                : null,
                        filterRequest.getPriceBetween() != null && filterRequest.getPriceBetween().size() == 2
                                ? filterRequest.getPriceBetween().get(1)
                                : null
                ))
                .and(StockSpecifications.byProductCategory(filterRequest.getCategory()))
                .and(StockSpecifications.byProductCategories(filterRequest.getCategories()))
                .and(StockSpecifications.byProductProvider(filterRequest.getProvider()))
                .and(StockSpecifications.byProductProviders(filterRequest.getProviders()))
                .and(StockSpecifications.byStockQuantity(filterRequest.getStock()))
                .and(StockSpecifications.byStockQuantityLessThan(filterRequest.getStockLessThan()))
                .and(StockSpecifications.byStockQuantityGreaterThan(filterRequest.getStockGreaterThan()))
                .and(StockSpecifications.byStockQuantityBetween(
                        filterRequest.getStockBetween() != null && filterRequest.getStockBetween().size() == 2
                                ? filterRequest.getStockBetween().get(0)
                                : null,
                        filterRequest.getStockBetween() != null && filterRequest.getStockBetween().size() == 2
                                ? filterRequest.getStockBetween().get(1)
                                : null
                ));
        Page<StockEntity> stock = stockRepository.findAll(spec, pageable);
        return stock.map(stockEntity ->
                productStoreMapper.toResponse(stockEntity.getProduct(), stockEntity.getQuantity()));
    }

    /**
     * Obtiene la información de un producto específico en una tienda específica.
     * 
     * @param storeID ID de la tienda donde se encuentra el producto
     * @param productID ID del producto a buscar
     * @return DTO con la información del producto y su cantidad en stock
     * @throws NotFoundException si no se encuentra el stock para el producto y tienda especificados
     */
    public ProductStoreResponse getProductByStoreAndProductID(Long storeID, Long productID) {
        StockEntity stock = stockRepository.findByProductIdAndStoreId(productID, storeID).orElseThrow(()->
                new NotFoundException("Stock not found for product id: " + productID + " and store id: " + storeID));
        return productStoreMapper.toResponse(stock.getProduct(), stock.getQuantity());
    }

    /**
     * Elimina el stock de un producto en una tienda específica.
     * 
     * @param productId ID del producto cuyo stock se eliminará
     * @param storeId ID de la tienda donde se eliminará el stock
     */
    public void removeStock(Long productId, Long storeId) {
        stockRepository.deleteByProductIdAndStoreId(productId, storeId);
    }

    /**
     * Obtiene la informacion de stock de un producto en una tienda específica.
     * 
     * @param productId ID del producto del que se busca el stock
     * @param storeId ID de la tienda donde se busca el stock
     * @return DTO con la información del stock
     * @throws NotFoundException si no se encuentra el producto, la tienda o el stock
     */
    public StockResponse getStock(Long productId, Long storeId) {
        StockEntity stock = findStockByProductAndStore(productId, storeId);
        return stockMapper.toResponse(stock);
    }

    /**
     * Agrega stock de un producto a una tienda.
     * 
     * @param productID ID del producto al que se agregará stock
     * @param storeID ID de la tienda donde se agregará el stock
     * @param request DTO con la información del stock a agregar
     * @return DTO con la información del stock agregado
     * @throws NotFoundException si no se encuentra el producto o la tienda
     */
    public StockResponse addStock(Long productID, Long storeID ,StockRequest request) {
        ProductEntity product = findProduct(productID);
        StoreEntity store = findStore(storeID);
        StockEntity stock = stockMapper.toEntity(request, product, store);

        return stockMapper.toResponse(stockRepository.save(stock));
    }

    /**
     * Actualiza la cantidad de stock de un producto en una tienda específica.
     * 
     * @param productID ID del producto cuyo stock se actualizará
     * @param storeID ID de la tienda donde se actualizará el stock
     * @param request DTO con la nueva cantidad de stock
     * @return DTO con la información del stock actualizado
     * @throws NotFoundException si no se encuentra el producto, la tienda o el stock
     */
    public StockResponse updateStock(Long productID, Long storeID, StockRequest request) {
        StockEntity stock = findStockByProductAndStore(productID,storeID);
        stock.setQuantity(request.quantity());

        return stockMapper.toResponse(stockRepository.save(stock));
    }

    /**
     * Transfiere stock de un producto de una tienda a otra.
     * 
     * @param originStoreID ID de la tienda de origen
     * @param transferRequest DTO con la información de la transferencia (producto, tienda destino, cantidad)
     * @return Lista con dos DTOs: el stock actualizado en la tienda de origen y el stock actualizado en la tienda de destino
     * @throws NotFoundException si no se encuentra el producto, alguna de las tiendas o el stock
     * @throws InsufficientStockException si no hay suficiente stock en la tienda de origen o si las tiendas son la misma
     */
    @Transactional
    public List<StockResponse> transferStock(Long originStoreID,
                                             StockTransferRequest transferRequest) {

        StockEntity stockFrom = findStockByProductAndStore(transferRequest.productId(),originStoreID);
        StockEntity stockTo = findStockByProductAndStore(transferRequest.productId(), transferRequest.destinationStoreId());

        Double quantityToTransfer = transferRequest.quantity();

        if (stockFrom.getQuantity() < quantityToTransfer) {
            throw new InsufficientStockException("Not enough stock in origin store to transfer.");
        }
        if (originStoreID.equals(transferRequest.destinationStoreId())) {
            throw new InsufficientStockException("Origin and destination store cannot be the same.");
        }

        stockFrom.setQuantity(stockFrom.getQuantity() - quantityToTransfer);
        stockTo.setQuantity(stockTo.getQuantity() + quantityToTransfer);

        StockResponse responseFrom = stockMapper.toResponse(stockRepository.save(stockFrom));
        StockResponse responseTo = stockMapper.toResponse(stockRepository.save(stockTo));

        return List.of(responseFrom, responseTo);
    }

    /**
     * Aumenta la cantidad de stock de un producto en una tienda específica.
     * Si la cantidad supera el umbral configurado, se resetea la bandera de alerta de stock bajo.
     * 
     * @param productId ID del producto cuyo stock se aumentará
     * @param storeId ID de la tienda donde se aumentará el stock
     * @param quantity Cantidad a aumentar
     * @return DTO con la información del stock actualizado
     * @throws NotFoundException si no se encuentra el producto, la tienda o el stock
     */
    public StockResponse increaseStock(Long productId, Long storeId, Double quantity) {
        StockEntity stock = findStockByProductAndStore(productId, storeId);
        stock.setQuantity(stock.getQuantity() + quantity);

        if(stock.getQuantity() > globalPreferencesConfig.getStockAlertThreshold()){
            stock.setLowStockAlertSent(false);
        }

        return stockMapper.toResponse(stockRepository.save(stock));
    }

    /**
     * Disminuye la cantidad de stock de un producto en una tienda específica.
     * Si la cantidad resultante es menor que el umbral configurado, se envía una alerta por correo.
     * 
     * @param productId ID del producto cuyo stock se disminuirá
     * @param storeId ID de la tienda donde se disminuirá el stock
     * @param quantity Cantidad a disminuir
     * @return DTO con la información del stock actualizado
     * @throws NotFoundException si no se encuentra el producto, la tienda o el stock
     * @throws InsufficientStockException si no hay suficiente stock para disminuir la cantidad especificada
     */
    public StockResponse decreaseStock(Long productId, Long storeId, Double quantity){
        StockEntity stock = findStockByProductAndStore(productId, storeId);
        if(stock.getQuantity() < quantity){
            throw new InsufficientStockException("Stock not enough to decrease");
        }
        stock.setQuantity(stock.getQuantity() - quantity);
        emailService.sendStockAlert(stock);
        return stockMapper.toResponse(stockRepository.save(stock));
    }

    /**
     * Mtodo auxiliar para buscar el stock de un producto en una tienda específica.
     * 
     * @param productId ID del producto
     * @param storeId ID de la tienda
     * @return La entidad de stock encontrada
     * @throws NotFoundException si no se encuentra el producto, la tienda o el stock
     */
    private StockEntity findStockByProductAndStore(Long productId, Long storeId) {
        productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + productId));

        storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException("Store not found with id: " + storeId));


            return stockRepository.findByProductIdAndStoreId(productId, storeId).orElseThrow(()->
                    new NotFoundException("Stock not found for product id: " + productId + " and store id: " + storeId));
    }

    /**
     * Metodo auxiliar para buscar un producto por su ID.
     * 
     * @param productId ID del producto a buscar
     * @return La entidad del producto encontrado
     * @throws NotFoundException si no se encuentra ningún producto con el ID especificado
     */
    private ProductEntity findProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + productId));
    }

    /**
     * Metodo auxiliar para buscar una tienda por su ID.
     * 
     * @param storeId ID de la tienda a buscar
     * @return La entidad de la tienda encontrada
     * @throws NotFoundException si no se encuentra ninguna tienda con el ID especificado
     */
    private StoreEntity findStore(Long storeId) {
       return  storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException("Store not found with id: " + storeId));
    }
}
