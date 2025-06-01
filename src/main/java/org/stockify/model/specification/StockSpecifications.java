package org.stockify.model.specification;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.stockify.model.entity.ProductEntity;
import org.stockify.model.entity.StockEntity;

import java.util.List;

public class StockSpecifications {

    public static Specification<StockEntity> byStoreId(Long storeId) {
        return (root, query, cb) -> cb.equal(root.get("store").get("id"), storeId);
    }

    public static Specification<StockEntity> byProductName(String name) {
        return (root, query, cb) -> {
            Join<StockEntity, ProductEntity> productJoin = root.join("product");
            return cb.like(productJoin.get("name"), "%" + name + "%");
        };
    }

    public static Specification<StockEntity> byProductProvider(String provider) {
        return (root, query, cb) -> {
            Join<StockEntity, ProductEntity> productJoin = root.join("product");
            return cb.like(cb.lower(productJoin.join("providers").get("name")), "%" + provider.toLowerCase() + "%");
        };
    }

    public static Specification<StockEntity> byProductCategory(String category) {
        return (root, query, cb) -> {
            Join<StockEntity, ProductEntity> productJoin = root.join("product");
            return cb.like(cb.lower(productJoin.join("categories").get("name")), "%" + category.toLowerCase() + "%");
        };
    }

    public static Specification<StockEntity> byProductSku(String sku) {
        return (root, query, cb) -> {
            Join<StockEntity, ProductEntity> productJoin = root.join("product");
            return cb.like(productJoin.get("sku"), "%" + sku + "%");
        };
    }

    public static Specification<StockEntity> byProductBarCode(String barCode) {
        return (root, query, cb) -> {
            Join<StockEntity, ProductEntity> productJoin = root.join("product");
            return cb.like(productJoin.get("barCode"), "%" + barCode + "%");
        };
    }

    public static Specification<StockEntity> byProductCategories(List<String> categories) {
        return (root, query, cb) -> {
            Join<StockEntity, ProductEntity> productJoin = root.join("product");
            return productJoin.join("categories").get("name").in(categories);
        };
    }

    public static Specification<StockEntity> byProductProviders(List<String> providers) {
        return (root, query, cb) -> {
            Join<StockEntity, ProductEntity> productJoin = root.join("product");
            return productJoin.join("providers").get("name").in(providers);
        };
    }

    public static Specification<StockEntity> byProductBrand(String brand) {
        return (root, query, cb) -> {
            Join<StockEntity, ProductEntity> productJoin = root.join("product");
            return cb.like(productJoin.get("brand"), "%" + brand + "%");
        };
    }

    public static Specification<StockEntity> byProductPrice(Double price) {
        return (root, query, cb) -> {
            Join<StockEntity, ProductEntity> productJoin = root.join("product");
            return cb.equal(productJoin.get("price"), price);
        };
    }

    public static Specification<StockEntity> byProductPriceGreaterThan(Double price) {
        return (root, query, cb) -> {
            Join<StockEntity, ProductEntity> productJoin = root.join("product");
            return cb.greaterThan(productJoin.get("price"), price);
        };
    }

    public static Specification<StockEntity> byProductPriceLessThan(Double price) {
        return (root, query, cb) -> {
            Join<StockEntity, ProductEntity> productJoin = root.join("product");
            return cb.lessThan(productJoin.get("price"), price);
        };
    }

    public static Specification<StockEntity> byProductPriceBetween(Double min, Double max) {
        return (root, query, cb) -> {
            Join<StockEntity, ProductEntity> productJoin = root.join("product");
            return cb.between(productJoin.get("price"), min, max);
        };
    }

    public static Specification<StockEntity> byProductDescription(String description) {
        return (root, query, cb) -> {
            Join<StockEntity, ProductEntity> productJoin = root.join("product");
            return cb.like(productJoin.get("description"), "%" + description + "%");
        };
    }

    public static Specification<StockEntity> byStockQuantity(Double stock) {
        return (root, query, cb) -> cb.equal(root.get("quantity"), stock);
    }

    public static Specification<StockEntity> byStockQuantityGreaterThan(Double stock) {
        return (root, query, cb) -> cb.greaterThan(root.get("quantity"), stock);
    }

    public static Specification<StockEntity> byStockQuantityLessThan(Double stock) {
        return (root, query, cb) -> cb.lessThan(root.get("quantity"), stock);
    }
    public static Specification<StockEntity> byStockQuantityBetween(Double min, Double max) {
        return (root, query, cb) -> {
            Join<StockEntity, ProductEntity> productJoin = root.join("stock");
            return cb.between(productJoin.get("quantity"), min, max);
        };
    }
}
