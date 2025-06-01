package org.stockify.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
public class ProductFilterRequest {
    private Double price;
    private Double Stock;
    private Double StockGreaterThan;
    private Double StockLessThan;
    @Size(min = 2,max = 2, message = "stockBetween need 2 params")
    private List<Double> StockBetween;
    private String name;
    private String description;
    private String barcode;
    private String sku;
    private String brand;
    private String provider;
    private String category;
    private List<String> categories;
    private List<String> providers;
    @Size(min = 2,max = 2, message = "priceBetween need 2 params")
    private List<Double> priceBetween;
    private Double priceGreater;
    private Double priceLess;
}