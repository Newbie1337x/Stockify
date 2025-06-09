package org.stockify.model.service;
import jakarta.transaction.Transactional;
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
import org.stockify.model.exception.InsufficientStockException;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.ProductStoreMapper;
import org.stockify.model.mapper.StockMapper;
import org.stockify.model.repository.ProductRepository;
import org.stockify.model.repository.StockRepository;
import org.stockify.model.repository.StoreRepository;
import org.stockify.model.specification.StockSpecifications;

import java.util.List;

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
        StockEntity stock = findStockByProductAndStore(productId, storeId);
        return stockMapper.toResponse(stock);
    }

    public StockResponse addStock(StockRequest request) {
        ProductEntity product = findProduct(request.productId());
        StoreEntity store = findStore(request.storeId());
        StockEntity stock = stockMapper.toEntity(request, product, store);

        return stockMapper.toResponse(stockRepository.save(stock));
    }

    public StockResponse updateStock(StockRequest request) {
        StockEntity stock = findStockByProductAndStore(request.productId(), request.storeId());
        stock.setQuantity(request.quantity());

        return stockMapper.toResponse(stockRepository.save(stock));
    }

    @Transactional
    public List<StockResponse> transferStock(Long storeIdFrom,
                                             StockRequest transferRequest) {

        StockEntity stockFrom = findStockByProductAndStore(transferRequest.productId(),storeIdFrom);
        StockEntity stockTo = findStockByProductAndStore(transferRequest.productId(), transferRequest.storeId());

        Double quantityToTransfer = transferRequest.quantity();

        if (stockFrom.getQuantity() < quantityToTransfer) {
            throw new InsufficientStockException("Not enough stock in origin store to transfer.");
        }
        if (storeIdFrom.equals(transferRequest.storeId())) {
            throw new InsufficientStockException("Origin and destination store cannot be the same.");
        }

        stockFrom.setQuantity(stockFrom.getQuantity() - quantityToTransfer);
        stockTo.setQuantity(stockTo.getQuantity() + quantityToTransfer);

        StockResponse responseFrom = stockMapper.toResponse(stockRepository.save(stockFrom));
        StockResponse responseTo = stockMapper.toResponse(stockRepository.save(stockTo));

        return List.of(responseFrom, responseTo);
    }

    public StockResponse increaseStock(Long productId, Long storeId, Double quantity) {
        StockEntity stock = findStockByProductAndStore(productId, storeId);
        stock.setQuantity(stock.getQuantity() + quantity);
        return stockMapper.toResponse(stockRepository.save(stock));
    }

        public StockResponse decreaseStock(Long productId, Long storeId, Double quantity){
            StockEntity stock = findStockByProductAndStore(productId, storeId);
        if(stock.getQuantity() < quantity){
            throw new InsufficientStockException("Stock not enough to decrease");
        }
        stock.setQuantity(stock.getQuantity() - quantity);
        return stockMapper.toResponse(stockRepository.save(stock));
    }

    private StockEntity findStockByProductAndStore(Long productId, Long storeId) {
        productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + productId));

        storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException("Store not found with id: " + storeId));


            return stockRepository.findByProductIdAndStoreId(productId, storeId).orElseThrow(()->
                    new NotFoundException("Stock not found for product id: " + productId + " and store id: " + storeId));
    }

    private ProductEntity findProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + productId));
    }

    private StoreEntity findStore(Long storeId) {
       return  storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException("Store not found with id: " + storeId));
    }
}
