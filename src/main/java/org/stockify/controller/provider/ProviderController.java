package org.stockify.controller.provider;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.provider.ProviderFilterRequest;
import org.stockify.dto.request.provider.ProviderRequest;
import org.stockify.dto.response.BulkProviderResponse;
import org.stockify.dto.response.ProviderResponse;
import org.stockify.model.assembler.ProductModelAssembler;
import org.stockify.model.assembler.ProviderModelAssembler;
import org.stockify.model.service.ProductService;
import org.stockify.model.service.ProviderService;
import java.util.List;

@RestController
@RequestMapping("/providers")
public class ProviderController {

    private final ProviderService providerService;
    private final ProviderModelAssembler providerModelAssembler;


    public ProviderController(ProviderService providerService,
                              ProductService productService,
                              ProviderModelAssembler providerModelAssembler,
                              ProductModelAssembler productModelAssembler) {

        this.providerService = providerService;
        this.providerModelAssembler = providerModelAssembler;

    }

    //---Crud operations---
    @Operation(summary = "List all providers with optional filters")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<ProviderResponse>>> listProviders(
            @Valid ProviderFilterRequest filters,
            @PageableDefault(sort = "name") Pageable pageable,
            PagedResourcesAssembler<ProviderResponse> assembler) {

        Page<ProviderResponse> providerPage = providerService.findAll(pageable, filters);
        PagedModel<EntityModel<ProviderResponse>> pagedModel =
                assembler.toModel(providerPage, providerModelAssembler);

        return ResponseEntity.ok(pagedModel);
    }

    @Operation(summary = "Get provider by ID")
    @GetMapping("/{providerID}")
    public ResponseEntity<EntityModel<ProviderResponse>> getProviderById(@PathVariable Long providerID) {
        ProviderResponse providerResponse = providerService.findById(providerID);
        EntityModel<ProviderResponse> entityModel = providerModelAssembler.toModel(providerResponse);
        return ResponseEntity.ok(entityModel);
    }

    @Operation(summary = "Create a new provider")
    @PostMapping
        public ResponseEntity<EntityModel<ProviderResponse>> createProvider(@Valid @RequestBody ProviderRequest request) {
        return ResponseEntity.ok(providerModelAssembler.toModel(providerService.save(request)));
    }

    @Operation(summary = "Create multiple providers")
    @PostMapping("/bulk")
    public ResponseEntity<BulkProviderResponse> bulkSaveProviders(@Valid @RequestBody List<@Valid ProviderRequest> providers) {
        return ResponseEntity.status(HttpStatus.CREATED).body(providerService.saveAll(providers));
    }

    @Operation(summary = "Logically delete a provider")
    @PatchMapping("/{providerID}/disable")
    public ResponseEntity<EntityModel<ProviderResponse>> logicalDeleteProvider(@PathVariable Long providerID) {
        return ResponseEntity.ok(providerModelAssembler.toModel(providerService.logicalDelete(providerID)));
    }

    @Operation(summary = "Delete a provider from the system")
    @DeleteMapping("/{providerID}")
    public ResponseEntity<EntityModel<ProviderResponse>> deleteProvider(@PathVariable Long providerID) {
        return ResponseEntity.ok(providerModelAssembler.toModel(providerService.logicalDelete(providerID)));
    }


    //Products Logic


}
