package org.stockify.controller.provider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.stockify.dto.request.provider.ProviderFilterRequest;
import org.stockify.dto.request.provider.ProviderRequest;
import org.stockify.dto.response.BulkProviderResponse;
import org.stockify.dto.response.ProviderResponse;
import org.stockify.model.assembler.ProviderModelAssembler;
import org.stockify.model.service.ProviderService;

import java.util.List;

@RestController
@RequestMapping("/providers")
@RequiredArgsConstructor
@Tag(name = "Providers", description = "Operations related to managing providers")
@SecurityRequirement(name = "bearerAuth")
public class ProviderController {

    private final ProviderService providerService;
    private final ProviderModelAssembler providerModelAssembler;

    @Operation(summary = "List all providers with optional filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paged list of providers returned successfully")
    })
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('READ') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('READ')")
    public ResponseEntity<PagedModel<EntityModel<ProviderResponse>>> listProviders(
            @ParameterObject @Valid ProviderFilterRequest filters,
            @Parameter(hidden = true)
            @PageableDefault(sort = "name") Pageable pageable,
            PagedResourcesAssembler<ProviderResponse> assembler) {

        Page<ProviderResponse> providerPage = providerService.findAll(pageable, filters);
        return ResponseEntity.ok(assembler.toModel(providerPage, providerModelAssembler));
    }

    @Operation(summary = "Get provider by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Provider found"),
            @ApiResponse(responseCode = "404", description = "Provider not found")
    })
    @GetMapping("/{providerID}")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('READ') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('READ')")
    public ResponseEntity<EntityModel<ProviderResponse>> getProviderById(
            @Parameter(description = "ID of the provider") @PathVariable Long providerID) {

        ProviderResponse providerResponse = providerService.findById(providerID);
        return ResponseEntity.ok(providerModelAssembler.toModel(providerResponse));
    }

    @Operation(summary = "Create a new provider")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Provider created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('WRITE') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('WRITE')")
    public ResponseEntity<EntityModel<ProviderResponse>> createProvider(
            @Valid @RequestBody ProviderRequest request) {

        return ResponseEntity.ok(providerModelAssembler.toModel(providerService.save(request)));
    }

    @Operation(summary = "Create multiple providers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bulk provider creation completed")
    })
    @PostMapping("/bulk")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('WRITE') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('WRITE')")
    public ResponseEntity<BulkProviderResponse> bulkSaveProviders(
            @Valid @RequestBody List<@Valid ProviderRequest> providers) {

        return ResponseEntity.status(HttpStatus.CREATED).body(providerService.saveAll(providers));
    }
        

    @Operation(summary = "Logically delete a provider")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Provider logically deleted"),
            @ApiResponse(responseCode = "404", description = "Provider not found")
    })
    @PatchMapping("/{providerID}/disable")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('WRITE') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('WRITE')")
    public ResponseEntity<EntityModel<ProviderResponse>> logicalDeleteProvider(
            @Parameter(description = "ID of the provider") @PathVariable Long providerID) {

        return ResponseEntity.ok(providerModelAssembler.toModel(providerService.logicalDelete(providerID)));
    }

    @Operation(summary = "Logically reactivate a provider")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Provider logically reactivated"),
            @ApiResponse(responseCode = "404", description = "Provider not found")
    })
    @PatchMapping("/{providerID}/enable")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('WRITE') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('WRITE')")
    public ResponseEntity<EntityModel<ProviderResponse>> logicalReactivateProvider(
            @Parameter(description = "ID of the provider") @PathVariable Long providerID) {

        return ResponseEntity.ok(providerModelAssembler.toModel(providerService.logicalReactivate(providerID)));
    }

    @Operation(summary = "Update a provider")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Provider updated successfully"),
            @ApiResponse(responseCode = "404", description = "Provider not found or inactive")
    })
    @PutMapping("/{providerID}")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('WRITE') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('WRITE')")
    public ResponseEntity<EntityModel<ProviderResponse>> updateProvider(
            @Parameter(description = "ID of the provider") @PathVariable Long providerID,
            @Valid @RequestBody ProviderRequest request) {

        return ResponseEntity.ok(providerModelAssembler.toModel(providerService.update(providerID, request)));
    }

    @Operation(summary = "Partially update a provider")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Provider updated successfully"),
            @ApiResponse(responseCode = "404", description = "Provider not found or inactive")
    })
    @PatchMapping("/{providerID}")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('WRITE') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('WRITE')")
    public ResponseEntity<EntityModel<ProviderResponse>> partialUpdateProvider(
            @Parameter(description = "ID of the provider") @PathVariable Long providerID,
            @Valid @RequestBody ProviderRequest request) {

        return ResponseEntity.ok(providerModelAssembler.toModel(providerService.update(providerID, request)));
    }
}
