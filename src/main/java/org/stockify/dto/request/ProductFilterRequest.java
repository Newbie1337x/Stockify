package org.stockify.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ProductFilterRequest {
    private Double price;
    private Double stock;
    private String name;
    private String description;
    private String barcode;
    private String sku;
    private String brand;
    private String provider;
    private String category;
    private List<String> categories;
    private List<String> providers;
}