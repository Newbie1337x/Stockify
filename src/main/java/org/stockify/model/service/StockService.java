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

    public Page<ProductStoreResponse> listProductsByStore(
            Long storeID, Pageable pageable, ProductFilterRequest filterRequest
    ) {
        Specification<StockEntity> spec = Specification.where(StockSpecifications.byStoreId(storeID));

        if (filterRequest != null) {
            if (filterRequest.getStock() != null) {
                spec = spec.and(StockSpecifications.byStockQuantity(filterRequest.getStock()));
            }
            if (filterRequest.getName() != null && !filterRequest.getName().isEmpty()) {
                spec = spec.and(StockSpecifications.byProductName(filterRequest.getName()));
            }
            if (filterRequest.getSku() != null && !filterRequest.getSku().isEmpty()) {
                spec = spec.and(StockSpecifications.byProductSku(filterRequest.getSku()));
            }
            if (filterRequest.getBarcode() != null && !filterRequest.getBarcode().isEmpty()) {
                spec = spec.and(StockSpecifications.byProductBarCode(filterRequest.getBarcode()));
            }
            if (filterRequest.getBrand() != null && !filterRequest.getBrand().isEmpty()) {
                spec = spec.and(StockSpecifications.byProductBrand(filterRequest.getBrand()));
            }
            if (filterRequest.getDescription() != null && !filterRequest.getDescription().isEmpty()) {
                spec = spec.and(StockSpecifications.byProductDescription(filterRequest.getDescription()));
            }
            if (filterRequest.getPrice() != null) {
                spec = spec.and(StockSpecifications.byProductPrice(filterRequest.getPrice()));
            }
            if (filterRequest.getPriceGreater() != null) {
                spec = spec.and(StockSpecifications.byProductPriceGreaterThan(filterRequest.getPriceGreater()));
            }
            if (filterRequest.getPriceLess() != null) {
                spec = spec.and(StockSpecifications.byProductPriceLessThan(filterRequest.getPriceLess()));
            }
            if (filterRequest.getPriceBetween() != null && filterRequest.getPriceBetween().size() == 2) {
                spec = spec.and(StockSpecifications.byProductPriceBetween(
                        filterRequest.getPriceBetween().get(0),
                        filterRequest.getPriceBetween().get(1)
                ));
            }
            if (filterRequest.getCategory() != null && !filterRequest.getCategory().isEmpty()) {
                spec = spec.and(StockSpecifications.byProductCategory(filterRequest.getCategory()));
            }
            if (filterRequest.getCategories() != null && !filterRequest.getCategories().isEmpty()) {
                spec = spec.and(StockSpecifications.byProductCategories(filterRequest.getCategories()));
            }
            if (filterRequest.getProvider() != null && !filterRequest.getProvider().isEmpty()) {
                spec = spec.and(StockSpecifications.byProductProvider(filterRequest.getProvider()));
            }
            if (filterRequest.getProviders() != null && !filterRequest.getProviders().isEmpty()) {
                spec = spec.and(StockSpecifications.byProductProviders(filterRequest.getProviders()));
            }
        }

        Page<StockEntity> stocks = stockRepository.findAll(spec, pageable);

        return stocks.map(stock -> productStoreMapper.toResponse(stock.getProduct(), stock.getQuantity()));
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

        Optional<StockEntity> existingStock = findStockByProductAndStore(request.productId(), request.storeId());

        if (existingStock.isPresent()) {
            throw new IllegalStateException("Stock already exists for product id: " + request.productId() + " and store id: " + request.storeId());
        }

        StockEntity stock = stockMapper.toEntity(request, product, store);
        stock = stockRepository.save(stock);
        return stockMapper.toResponse(stock);
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
