package org.stockify.controller.store;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.store.StoreRequest;
import org.stockify.dto.response.StoreResponse;
import org.stockify.model.assembler.StoreModelAssembler;
import org.stockify.model.service.StoreService;

import java.net.URI;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
@Tag(name = "Stores", description = "Operations related to store management")
@SecurityRequirement(name = "bearerAuth")
public class StoreController {

    private final StoreService storeService;
    private final StoreModelAssembler storeModelAssembler;

    @Operation(summary = "List all stores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paged list of stores returned successfully")
    })
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('READ')")
    public ResponseEntity<PagedModel<EntityModel<StoreResponse>>> getAllStores(
            @Parameter(hidden = true) Pageable pageable,
            PagedResourcesAssembler<StoreResponse> assembler) {
        Page<StoreResponse> stores = storeService.findAll(pageable);
        return ResponseEntity.ok(assembler.toModel(stores, storeModelAssembler));
    }

    @Operation(summary = "Get store by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Store found"),
            @ApiResponse(responseCode = "404", description = "Store not found")
    })
    @GetMapping("/{storeID}")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('READ') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('READ')")
    public ResponseEntity<EntityModel<StoreResponse>> getStoreById(
            @Parameter(description = "ID of the store") @PathVariable Long storeID) {
        StoreResponse store = storeService.findById(storeID);
        return ResponseEntity.ok(storeModelAssembler.toModel(store));
    }

    @Operation(summary = "Create a new store")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Store created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('WRITE')")
    public ResponseEntity<EntityModel<StoreResponse>> createStore(
            @Valid @RequestBody StoreRequest request) {
        StoreResponse store = storeService.save(request);
        EntityModel<StoreResponse> storeModel = storeModelAssembler.toModel(store);
        return ResponseEntity.created(URI.create("/stores/" + store.id())).body(storeModel);
    }

    @Operation(summary = "Update store by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Store updated successfully"),
            @ApiResponse(responseCode = "404", description = "Store not found"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PutMapping("/{storeID}")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('WRITE')")
    public ResponseEntity<EntityModel<StoreResponse>> updateStore(
            @Parameter(description = "ID of the store") @PathVariable Long storeID,
            @Valid @RequestBody StoreRequest request) {
        StoreResponse store = storeService.update(storeID, request);
        return ResponseEntity.ok(storeModelAssembler.toModel(store));
    }

    @Operation(summary = "Delete store by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Store deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Store not found")
    })
    @DeleteMapping("/{storeID}")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('DELETE')")
    public ResponseEntity<Void> deleteStore(
            @Parameter(description = "ID of the store") @PathVariable Long storeID) {
        storeService.deleteById(storeID);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Patch store by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Store patched successfully"),
            @ApiResponse(responseCode = "404", description = "Store not found")
    })
    @PatchMapping("/{storeID}")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('WRITE')")
    public ResponseEntity<EntityModel<StoreResponse>> patchStore(
            @Parameter(description = "ID of the store") @PathVariable Long storeID,
            @RequestBody StoreRequest request) {
        StoreResponse store = storeService.patch(storeID, request);
        return ResponseEntity.ok(storeModelAssembler.toModel(store));
    }
}
