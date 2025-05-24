package org.stockify.controller;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.AssignProductRequest;
import org.stockify.dto.request.ProviderRequest;
import org.stockify.dto.response.BulkProviderResponse;
import org.stockify.dto.response.ProductResponse;
import org.stockify.dto.response.ProviderResponse;
import org.stockify.model.assembler.ProviderModelAssembler;
import org.stockify.model.mapper.ProviderMapper;
import org.stockify.model.service.ProductService;
import org.stockify.model.service.ProviderService;

import java.security.Provider;
import java.util.List;


@RestController
@RequestMapping("/api/provider")
public class ProviderController {

    private final ProviderService providerService;
    private final ProductService productService;
    private final ProviderModelAssembler providerModelAssembler;

    public ProviderController(ProviderService providerService,
                              ProductService productService,
                              ProviderModelAssembler providerModelAssembler) {

        this.providerService = providerService;
        this.productService = productService;
        this.providerModelAssembler = providerModelAssembler;
    }

    //---Crud operations---
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<ProviderResponse>>> listProviders(
            @PageableDefault(sort = "name") Pageable pageable,
            PagedResourcesAssembler<ProviderResponse> assembler) {

        Page<ProviderResponse> providerPage = providerService.findAll(pageable);
        PagedModel<EntityModel<ProviderResponse>> pagedModel =
                assembler.toModel(providerPage, providerModelAssembler);

        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ProviderResponse>> getProviderById(@PathVariable Long id) {
        ProviderResponse providerResponse = providerService.findById(id);
        EntityModel<ProviderResponse> entityModel = providerModelAssembler.toModel(providerResponse);
        return ResponseEntity.ok(entityModel);
    }

    @PostMapping
        public ResponseEntity<EntityModel<ProviderResponse>> createProvider(@Valid @RequestBody ProviderRequest request) {
        return ResponseEntity.ok(providerModelAssembler.toModel(providerService.save(request)));
    }

    @PostMapping("/bulk")
    public ResponseEntity<BulkProviderResponse> bulkSaveProviders(@Valid @RequestBody List<@Valid ProviderRequest> providers) {
        return ResponseEntity.status(HttpStatus.CREATED).body(providerService.saveAll(providers));
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<String> logicalDeleteProvider(@PathVariable Long id) {
        return ResponseEntity.ok("The following provider has been disabled: " + providerService.logicalDelete(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProvider(@PathVariable Long id) {
        return ResponseEntity.ok("The following provider has been deleted: " + providerService.delete(id));
    }


    //Products Logic

    @GetMapping("/{id}/products")
    public ResponseEntity<PagedModel<EntityModel<ProductResponse>>> listProducts(
            @PathVariable Long id,
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
            PagedResourcesAssembler<ProductResponse> assembler
    ) {
        Page<ProductResponse> productPage = productService.findProductsByProviderId(id, pageable);
        return ResponseEntity.ok(assembler.toModel(productPage));
    }
    @PutMapping("/{id}/products")
    public ResponseEntity<EntityModel<ProviderResponse>> assingProducts(
            @PathVariable Long id,
            @RequestBody AssignProductRequest request
    ){
        return ResponseEntity.ok(providerModelAssembler
                .toModel(providerService.assignProductsToProvider(id,request.getProductsId())));
    }

    /*
    public ResponseEntity<EntityModel<ProviderResponse>> assingProduct(

    )*/
    @PatchMapping("/{id}/products")
    public ResponseEntity<EntityModel<ProviderResponse>> unassignProducts(
            @PathVariable Long id,
            @RequestBody AssignProductRequest request
    )
    {
        return ResponseEntity.ok(providerModelAssembler.toModel(providerService.unassignProductsFromProvider(id,request.getProductsId())));

    }

    //FILTERS //CONSULTAR

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String businessName,
            @RequestParam(required = false) String taxId,
            Pageable pageable) {

        if (name != null) {
            return ResponseEntity.ok(providerService.findByName(pageable, name));
        } else if (businessName != null) {
            return ResponseEntity.ok(providerService.findByBusinessName(businessName));
        } else if (taxId != null) {
            return ResponseEntity.ok(providerService.findByTaxId(taxId));
        } else {
            return listProviders(pageable, null);
        }
    }
}
