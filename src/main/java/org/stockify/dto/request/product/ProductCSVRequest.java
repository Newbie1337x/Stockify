package org.stockify.dto.request.product;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductCSVRequest {
    @CsvBindByName(column = "name")
    private String name;

    @CsvBindByName(column = "description")
    private String description;

    @CsvBindByName(column = "price")
    private BigDecimal price;

    @CsvBindByName(column = "unit_price")
    private BigDecimal unitPrice;

    @CsvBindByName(column = "stock")
    private BigDecimal stock;

    @CsvBindByName(column = "sku")
    private String sku;

    @CsvBindByName(column = "barcode")
    private String barcode;

    @CsvBindByName(column = "brand")
    private String brand;

    @CsvBindByName(column = "categories") // se puede agregar varias categorias separadas por comas
    private String categories;
}

