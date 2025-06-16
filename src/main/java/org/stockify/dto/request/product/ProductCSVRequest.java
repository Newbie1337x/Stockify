package org.stockify.dto.request.product;

import com.opencsv.bean.CsvBindByName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductCSVRequest {

    @Schema(description = "Name of the product", example = "Smartphone X")
    @CsvBindByName(column = "name")
    private String name;

    @Schema(description = "Description of the product", example = "Latest smartphone model")
    @CsvBindByName(column = "description")
    private String description;

    @Schema(description = "Price of the product", example = "999.99")
    @CsvBindByName(column = "price")
    private BigDecimal price;

    @Schema(description = "Unit price of the product", example = "800.00")
    @CsvBindByName(column = "unit_price")
    private BigDecimal unitPrice;

    @Schema(description = "Stock quantity available", example = "50")
    @CsvBindByName(column = "stock")
    private BigDecimal stock;

    @Schema(description = "SKU (Stock Keeping Unit) code", example = "PHONE-001")
    @CsvBindByName(column = "sku")
    private String sku;

    @Schema(description = "Barcode of the product", example = "BARCODE-001")
    @CsvBindByName(column = "barcode")
    private String barcode;

    @Schema(description = "Brand of the product", example = "TechBrand")
    @CsvBindByName(column = "brand")
    private String brand;

    @Schema(description = "Comma-separated list of categories", example = "Electronics,Mobile")
    @CsvBindByName(column = "categories") // multiple categories separated by commas
    private String categories;
}
