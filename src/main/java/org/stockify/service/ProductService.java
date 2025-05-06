package org.stockify.service;
import org.springframework.stereotype.Service;
import org.stockify.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    /*
   public ProductEntity findById(int id){
        return productRepository.findById(id)
    }
 */
}
