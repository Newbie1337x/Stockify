package org.stockify.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.client.ClientFilterRequest;
import org.stockify.dto.request.client.ClientRequest;
import org.stockify.dto.response.ClientResponse;
import org.stockify.model.assembler.ClientModelAssembler;
import org.stockify.model.service.ClientService;

import java.net.URI;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
@Tag(name = "Clients", description = "Endpoints for managing clients")
public class ClientController {

    private final ClientService clientService;
    private final ClientModelAssembler clientModelAssembler;

    @Operation(summary = "Create a new client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PreAuthorize("hasRole('ROLE_MANAGER') and hasAuthority('WRITE') or " +
            "hasRole('ROLE_ADMIN') and hasAuthority('WHITE')")
    @PostMapping
    public ResponseEntity<EntityModel<ClientResponse>> createClient(
            @Valid @RequestBody ClientRequest client) {

        ClientResponse clientResponse = clientService.save(client);
        EntityModel<ClientResponse> entityModel = clientModelAssembler.toModel(clientResponse);

        return ResponseEntity
                .created(URI.create(entityModel.getRequiredLink("self").getHref()))
                .body(entityModel);
    }

    @Operation(summary = "List all clients with optional filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paged list of clients retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<ClientResponse>>> getAllClients(
            @ParameterObject ClientFilterRequest filterRequest,
            @Parameter(description = "Page number", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20") @RequestParam(defaultValue = "20") int size,
            PagedResourcesAssembler<ClientResponse> pagedAssembler) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("firstName").ascending());
        Page<ClientResponse> clientResponsePage = clientService.findAll(filterRequest, pageable);

        return ResponseEntity.ok(pagedAssembler.toModel(clientResponsePage, clientModelAssembler));
    }

    @Operation(summary = "Get client by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client found"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @GetMapping("/{clientID}")
    public ResponseEntity<EntityModel<ClientResponse>> getClientById(
            @Parameter(description = "ID of the client") @PathVariable Long clientID) {

        ClientResponse clientResponse = clientService.findById(clientID);
        return ResponseEntity.ok(clientModelAssembler.toModel(clientResponse));
    }

    @Operation(summary = "Delete a client by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Client deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @DeleteMapping("/{clientID}")
    public ResponseEntity<Void> deleteClientById(
            @Parameter(description = "ID of the client") @PathVariable Long clientID) {

        clientService.delete(clientID);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Partially update a client by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client updated successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @PatchMapping("/{clientID}")
    public ResponseEntity<EntityModel<ClientResponse>> patchClient(
            @Parameter(description = "ID of the client") @PathVariable Long clientID,
            @RequestBody ClientRequest client) {

        ClientResponse updatedClient = clientService.updateClientPartial(clientID, client);
        return ResponseEntity.ok(clientModelAssembler.toModel(updatedClient));
    }

    @Operation(summary = "Fully update a client by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @PutMapping("/{clientID}")
    public ResponseEntity<EntityModel<ClientResponse>> putClient(
            @Parameter(description = "ID of the client") @PathVariable Long clientID,
            @Valid @RequestBody ClientRequest client) {

        ClientResponse updatedClient = clientService.updateClientFull(clientID, client);
        return ResponseEntity.ok(clientModelAssembler.toModel(updatedClient));
    }
}
