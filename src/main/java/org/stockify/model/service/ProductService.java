package org.stockify.model.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.stockify.model.dto.request.ProductRequest;
import org.stockify.model.dto.response.ProductResponse;
import org.stockify.model.entity.ProductEntity;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.ProductMapper;
import org.stockify.model.repository.ProductRepository;


import java.util.List;

@Service
public class ProductService {
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);


    public ProductService(ProductMapper productMapper, ProductRepository productRepository) {
        this.productMapper = productMapper;
        this.productRepository = productRepository;
    }


    public ProductResponse findById(int id) throws NotFoundException{

        return productMapper.toResponse(
                productRepository
                        .findById(id)
                        .orElseThrow(()-> new NotFoundException("Product with ID " + id + " not found")));
    }

    public List<ProductResponse> findAll(){
        List<ProductResponse> products = productMapper.toResponseList(productRepository.findAll());

        if(products.isEmpty()){
            logger.warn("products list is empty");
        }
        return products;
    }

    public ProductResponse save(ProductRequest productRequest) {
        return productMapper.toResponse(productRepository.save(productMapper.toEntity(productRequest)));
    }


    public void deleteById(int id){
        productRepository.deleteById(id);
    }

    public void update(int id,ProductRequest productRequest) throws NotFoundException{

        ProductEntity old = productRepository.findById(id).orElseThrow(()-> new NotFoundException("Product with ID " + id + " not found"));



        old.setName(productRequest.name());
        old.setPrice(productRequest.price());
        old.setDescription(productRequest.description());

        productRepository.save(old);
    }

    public void patch(int id, ProductRequest productRequest) throws NotFoundException {

        ProductEntity old = productRepository.findById(id).orElseThrow(()-> new NotFoundException("Product with ID " + id + " not found"));

        if (productRequest.name() != null) {
            old.setName(productRequest.name());
        }

        //Implementar modificacion de stock

        if (productRequest.price() != null) {
            old.setPrice(productRequest.price());
        }
        if (productRequest.description() != null) {
            old.setDescription(productRequest.description());
        }
        productRepository.save(old);
    }



}
