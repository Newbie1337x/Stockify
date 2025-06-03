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
        if (name == null) {
            return (root, query, cb) -> cb.conjunction();
        }
        return (root, query, cb) -> {
            Join<StockEntity, ProductEntity> productJoin = root.join("product");
            return cb.like(productJoin.get("name"), "%" + name + "%");
        };
    }

    public static Specification<StockEntity> byProductProvider(String provider) {
        if (provider == null) {
            return (root, query, cb) -> cb.conjunction();
        }
        return (root, query, cb) -> {
            Join<StockEntity, ProductEntity> productJoin = root.join("product");
            return cb.like(cb.lower(productJoin.join("providers").get("name")), "%" + provider.toLowerCase() + "%");
        };
    }

    public static Specification<StockEntity> byProductCategory(String category) {
        if (category == null) {
            return (root, query, cb) -> cb.conjunction();
        }
        return (root, query, cb) -> {
            Join<StockEntity, ProductEntity> productJoin = root.join("product");
            return cb.like(cb.lower(productJoin.join("categories").get("name")), "%" + category.toLowerCase() + "%");
        };
    }

    public static Specification<StockEntity> byProductSku(String sku) {
        if (sku == null) {
            return (root, query, cb) -> cb.conjunction();
        }
        return (root, query, cb) -> {
            Join<StockEntity, ProductEntity> productJoin = root.join("product");
            return cb.like(productJoin.get("sku"), "%" + sku + "%");
        };
    }

    public static Specification<StockEntity> byProductBarCode(String barCode) {
        if (barCode == null) {
            return (root, query, cb) -> cb.conjunction();
        }
        return (root, query, cb) -> {
            Join<StockEntity, ProductEntity> productJoin = root.join("product");
            return cb.like(productJoin.get("barcode"), "%" + barCode + "%");
        };
    }

    public static Specification<StockEntity> byProductCategories(List<String> categories) {
        if (categories == null || categories.isEmpty()) {
            return (root, query, cb) -> cb.conjunction();
        }
        return (root, query, cb) -> {
            query.distinct(true);
            Join<StockEntity, ProductEntity> productJoin = root.join("product");
            return cb.or(categories.stream()
                    .map(category -> cb.like(cb.lower(productJoin.join("categories").get("name")), "%" + category.toLowerCase() + "%"))
                    .toArray(jakarta.persistence.criteria.Predicate[]::new));
        };
    }

    public static Specification<StockEntity> byProductProviders(List<String> providers) {
        if (providers == null || providers.isEmpty()) {
            return (root, query, cb) -> cb.conjunction();
        }
        return (root, query, cb) -> {
            query.distinct(true);
            Join<StockEntity, ProductEntity> productJoin = root.join("product");
            return cb.or(providers.stream()
                    .map(provider -> cb.like(cb.lower(productJoin.join("providers").get("name")), "%" + provider.toLowerCase() + "%"))
                    .toArray(jakarta.persistence.criteria.Predicate[]::new));
        };
    }

    public static Specification<StockEntity> byProductBrand(String brand) {
        if (brand == null) {
            return (root, query, cb) -> cb.conjunction();
        }
        return (root, query, cb) -> {
            Join<StockEntity, ProductEntity> productJoin = root.join("product");
            return cb.like(productJoin.get("brand"), "%" + brand + "%");
        };
    }

    public static Specification<StockEntity> byProductPrice(Double price) {
        if (price == null) {
            return (root, query, cb) -> cb.conjunction();
        }
        return (root, query, cb) -> {
            Join<StockEntity, ProductEntity> productJoin = root.join("product");
            return cb.equal(productJoin.get("price"), price);
        };
    }

    public static Specification<StockEntity> byProductPriceGreaterThan(Double price) {
        if (price == null) {
            return (root, query, cb) -> cb.conjunction();
        }
        return (root, query, cb) -> {
            Join<StockEntity, ProductEntity> productJoin = root.join("product");
            return cb.greaterThan(productJoin.get("price"), price);
        };
    }

    public static Specification<StockEntity> byProductPriceLessThan(Double price) {
        if (price == null) {
            return (root, query, cb) -> cb.conjunction();
        }
        return (root, query, cb) -> {
            Join<StockEntity, ProductEntity> productJoin = root.join("product");
            return cb.lessThan(productJoin.get("price"), price);
        };
    }

    public static Specification<StockEntity> byProductPriceBetween(Double min, Double max) {
        if (min == null || max == null) {
            return (root, query, cb) -> cb.conjunction();
        }
        return (root, query, cb) -> {
            Join<StockEntity, ProductEntity> productJoin = root.join("product");
            return cb.between(productJoin.get("price"), min, max);
        };
    }

    public static Specification<StockEntity> byProductDescription(String description) {
        if (description == null) {
            return (root, query, cb) -> cb.conjunction();
        }
        return (root, query, cb) -> {
            Join<StockEntity, ProductEntity> productJoin = root.join("product");
            return cb.like(productJoin.get("description"), "%" + description + "%");
        };
    }

    public static Specification<StockEntity> byStockQuantity(Double stock) {
        if (stock == null) {
            return (root, query, cb) -> cb.conjunction();
        }
        return (root, query, cb) -> cb.equal(root.get("quantity"), stock);
    }

    public static Specification<StockEntity> byStockQuantityGreaterThan(Double stock) {
        if (stock == null) {
            return (root, query, cb) -> cb.conjunction();
        }
        return (root, query, cb) -> cb.greaterThan(root.get("quantity"), stock);
    }

    public static Specification<StockEntity> byStockQuantityLessThan(Double stock) {
        if (stock == null) {
            return (root, query, cb) -> cb.conjunction();
        }
        return (root, query, cb) -> cb.lessThan(root.get("quantity"), stock);
    }
    public static Specification<StockEntity> byStockQuantityBetween(Double min, Double max) {
        if (min == null || max == null) {
            return (root, query, cb) -> cb.conjunction();
        }
        return (root, query, cb) -> cb.between(root.get("quantity"), min, max);
    }
}
