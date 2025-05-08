package org.stockify.model.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.stockify.model.dto.request.CategoryRequest;
import org.stockify.model.dto.request.ProductRequest;
import org.stockify.model.dto.response.ProductResponse;
import org.stockify.model.entity.CategoryEntity;
import org.stockify.model.entity.ProductEntity;
import org.stockify.model.exception.DuplicatedUniqueConstraintException;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.CategoryMapper;
import org.stockify.model.mapper.ProductMapper;
import org.stockify.model.repository.CategoryRepository;
import org.stockify.model.repository.ProductRepository;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final CategoryRepository categoryRepository;


    public ProductService(ProductMapper productMapper, ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productMapper = productMapper;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
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

        if (productRepository.existsByNameIgnoreCase(productRequest.name())) {
            throw new DuplicatedUniqueConstraintException("Product with name " + productRequest.name() + " already exists");
        }

        Set<CategoryEntity> categorias = productRequest.categories().stream()
                .map(nombre -> categoryRepository.findByName(nombre)
                        .orElseGet(() -> {
                            CategoryEntity nueva = new CategoryEntity();
                            nueva.setName(nombre);
                            return categoryRepository.save(nueva);
                        }))
                .collect(Collectors.toSet());

        ProductEntity producto = productMapper.toEntity(productRequest);
        producto.setCategories(categorias);

        return productMapper.toResponse(productRepository.save(producto));
    }


    public void deleteById(int id){
        productRepository.deleteById(id);
    }

    public ProductResponse update(int id,ProductRequest productRequest) throws NotFoundException{

        ProductEntity old = productRepository.findById(id).orElseThrow(()-> new NotFoundException("Product with ID " + id + " not found"));

        if (productRepository.existsByNameIgnoreCaseAndIdNot(productRequest.name(),id)){
            throw new DuplicatedUniqueConstraintException("Product with name " + productRequest.name() + " already exists");
        }

        old.setName(productRequest.name());
        old.setPrice(productRequest.price());
        old.setDescription(productRequest.description());
        old.setStock(productRequest.stock());
        return productMapper.toResponse(productRepository.save(old));
    }

    public ProductResponse patch(int id, ProductRequest req) throws NotFoundException {
        ProductEntity old = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product con ID " + id + " no encontrado"));

        if (req.name() != null)        old.setName(req.name());
        if (req.price() != null)       old.setPrice(req.price());
        if (req.description() != null) old.setDescription(req.description());
        if (req.stock() != null)       old.setStock(req.stock());

        if (req.categories() != null && !req.categories().isEmpty()) {
            Set<CategoryEntity> categoriasFinales = req.categories().stream()
                    .map(nombre -> categoryRepository.findByName(nombre)
                            .orElseGet(() -> {
                                CategoryEntity nueva = new CategoryEntity();
                                nueva.setName(nombre);
                                return categoryRepository.save(nueva); // crea si no existe
                            }))
                    .collect(Collectors.toSet());

            old.getCategories().addAll(categoriasFinales);
        }

        ProductEntity saved = productRepository.save(old);
        return productMapper.toResponse(saved);
    }



    public List<ProductResponse> filterByCategories(Set<Integer> categories) throws NotFoundException {
       return productMapper.toResponseList(productRepository.findDistinctByCategoriesId(categories,categories.size()));
    }

    public ProductResponse removeCategoryFromProduct(int idCategory, int idProduct){
        ProductEntity product = productRepository.findById(idProduct).orElseThrow(()-> new NotFoundException("Product with ID " + idProduct + " not found"));
        CategoryEntity category = categoryRepository.findById(idCategory).orElseThrow(()-> new NotFoundException("Category with ID " + idCategory + " not found"));
        product.getCategories().remove(category);
        productRepository.save(product);
        return productMapper.toResponse(product);
    }

    public ProductResponse removeAllCategoryFromProduct(int idProduct){
        ProductEntity product = productRepository.findById(idProduct).orElseThrow(()-> new NotFoundException("Product with ID " + idProduct + " not found"));
        product.getCategories().clear();
        productRepository.save(product);
        return productMapper.toResponse(product);
    }


}
