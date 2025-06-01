package org.stockify.model.specification;

import org.springframework.data.jpa.domain.Specification;
import org.stockify.model.entity.ProductEntity;
import java.util.List;

public class ProductSpecifications {

    public static Specification<ProductEntity> byName(String name) {
        return (root, query, cb) -> cb.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<ProductEntity> byProvider(String provider) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.join("providers").get("name")), "%" + provider.toLowerCase() + "%");
    }

    public static Specification<ProductEntity> byCategory(String category) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.join("categories").get("name")), "%" + category.toLowerCase() + "%");
    }

    public static Specification<ProductEntity> bySku (String sku) {
        return (root, query, cb) ->
                cb.like(root.get("sku"), "%" + sku + "%");
    }

    public static Specification<ProductEntity> byBarCode(String barCode) {
    return (root, query, cb) ->
            cb.like(root.get("barCode"), "%" + barCode + "%");
    }

    public static Specification<ProductEntity> byCategories(List<String> categories) {
        return (root, query, cb) ->
                root.join("categories").get("name").in(categories);
    }

    public static Specification<ProductEntity> byProviders(List<String> providers) {
        return (root, query, cb) ->
                root.join("providers").get("name").in(providers);
    }

    public static Specification<ProductEntity> byBrand(String brand) {
    return (root, query, cb) ->
            cb.like(root.get("brand"), "%" + brand + "%");
    }

    public static Specification<ProductEntity> byPrice(Double price) {
    return (root, query, cb) ->
            cb.equal(root.get("price"), price);
    }

    public static Specification<ProductEntity> byPriceGreaterThan(Double price) {
        return (root, query, cb) ->
                cb.greaterThan(root.get("price"), price);
    }

    public static Specification<ProductEntity> byPriceLessThan(Double price) {
    return (root, query, cb) ->
            cb.lessThan(root.get("price"), price);
    }

    public static Specification<ProductEntity> byPriceBetween(Double min, Double max) {
        return (root, query, cb) ->
                cb.between(root.get("price"), min, max);
    }

    public static Specification<ProductEntity> byDescription(String description) {
        return (root, query, cb) ->
                cb.like(root.get("description"), "%" + description + "%");
    }

    public static Specification<ProductEntity> byStock(Double stock) {
    return (root, query, cb) ->
            cb.equal(root.get("stock"), stock);
    }

    public static Specification<ProductEntity> byStockLessThan(Double stockLessThan) {
        return (root, query, cb) ->
                cb.lessThan(root.get("stock"), stockLessThan);
    }

    public static Specification<ProductEntity> byStockGreaterThan(Double stockGreaterThan) {
        return (root, query, cb) ->
                cb.greaterThan(root.get("stock"), stockGreaterThan);
    }

    public static Specification<ProductEntity> byStockBetween(Double aDouble, Double aDouble1) {
        return (root, query, cb) ->
                cb.between(root.get("stock"), aDouble, aDouble1);
    }
}
