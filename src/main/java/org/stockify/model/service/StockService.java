package org.stockify.model.service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.ProductFilterRequest;
import org.stockify.dto.request.StockRequest;
import org.stockify.dto.response.ProductStoreResponse;
import org.stockify.dto.response.StockResponse;
import org.stockify.model.entity.ProductEntity;
import org.stockify.model.entity.StockEntity;
import org.stockify.model.entity.StoreEntity;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.ProductStoreMapper;
import org.stockify.model.mapper.StockMapper;
import org.stockify.model.repository.ProductRepository;
import org.stockify.model.repository.StockRepository;
import org.stockify.model.repository.StoreRepository;
import org.stockify.model.specification.StockSpecifications;

import java.util.Optional;

@Service
public class StockService {
    private final StockRepository stockRepository;
    private final ProductStoreMapper productStoreMapper;
    private final StockMapper stockMapper;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    public StockService(
            StockRepository stockRepository,
            ProductStoreMapper productStoreMapper,
            StockMapper stockMapper,
            ProductRepository productRepository,
            StoreRepository storeRepository) {
        this.stockRepository = stockRepository;
        this.productStoreMapper = productStoreMapper;
        this.stockMapper = stockMapper;
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
    }

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

    public void removeStock(Long productId, Long storeId) {
        stockRepository.deleteByProductIdAndStoreId(productId, storeId);
    }

    public StockResponse getStock(Long productId, Long storeId) {
        return findStockByProductAndStore(productId, storeId)
                .map(stockMapper::toResponse)
                .orElseThrow(() -> generateNotFoundException(productId, storeId));
    }

    public StockResponse addStock(StockRequest request) {
        ProductEntity product = findProduct(request.productId());
        StoreEntity store = findStore(request.storeId());

        StockEntity stock = stockMapper.toEntity(request,product,store);
        return stockMapper.toResponse(stockRepository.save(stock));

    }

    public StockResponse updateStock(StockRequest request) {
        findProduct(request.productId());
        findStore(request.storeId());

        StockEntity stock = findStockByProductAndStore(request.productId(), request.storeId())
                .orElseThrow(() -> generateNotFoundException(request.productId(), request.storeId()));

        stock.setQuantity(request.quantity());
        stock = stockRepository.save(stock);
        return stockMapper.toResponse(stock);
    }

    public StockResponse increaseStock(Long productId, Long storeId, Long quantity) {
        findProduct(productId);
        findStore(storeId);
        StockEntity stock = findStockByProductAndStore(productId, storeId)
                .orElseThrow(() -> generateNotFoundException(productId, storeId));
        stock.setQuantity(stock.getQuantity() + quantity);
        return stockMapper.toResponse(stockRepository.save(stock));
    }

    public StockResponse decreaseStock(Long productId, Long storeId, Long quantity){
        findProduct(productId);
        findStore(storeId);
        StockEntity stock = findStockByProductAndStore(productId, storeId)
                .orElseThrow(() -> generateNotFoundException(productId, storeId));
        if(stock.getQuantity() < quantity){
            throw new NotFoundException("Stock not enough to decrease");
        }
        stock.setQuantity(stock.getQuantity() - quantity);
        return stockMapper.toResponse(stockRepository.save(stock));
    }

    private Optional<StockEntity> findStockByProductAndStore(Long productId, Long storeId) {
        return stockRepository.findByProductIdAndStoreId(productId, storeId);
    }

    private ProductEntity findProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + productId));
    }

    private StoreEntity findStore(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException("Store not found with id: " + storeId));
    }
    

    private NotFoundException generateNotFoundException(Long productId, Long storeId) {
        return new NotFoundException("Stock not found for product id: " + productId + " and store id: " + storeId);
    }
}
