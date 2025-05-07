package org.stockify.service;
import org.springframework.stereotype.Service;
import org.stockify.model.ProductEntity;
import org.stockify.repository.ProductRepository;
import org.stockify.service.exceptions.ProductNotFoundException;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }


   public ProductEntity findById(int id){
        return productRepository.findById(id)
                .orElseThrow(()-> new ProductNotFoundException("Product with ID " + id + " not found"));
    }

    public List<ProductEntity>  findAll(){
        return productRepository.findAll();
    }

    

}
