package org.stockify.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.ClientRequest;
import org.stockify.dto.response.ClientResponse;
import org.stockify.model.hateoas.ClientModelAssembler;
import org.stockify.model.services.ClientService;

@RestController
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;
    private final ClientModelAssembler clientModelAssembler;

    public ClientController(ClientService clientService, ClientModelAssembler clientModelAssembler) {
        this.clientService = clientService;
        this.clientModelAssembler = clientModelAssembler;
    }

    @PostMapping
    public ResponseEntity<ClientResponse> createClient(@Valid @RequestBody ClientRequest client) {
        ClientResponse clientResponse = clientService.save(client);
        return new ResponseEntity<>(clientResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<ClientResponse>>> getAllClients(
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "20") int size,
                                                                    PagedResourcesAssembler<ClientResponse> pagedAssembler) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("firstName").ascending());
        Page<ClientResponse> clientResponsePage = clientService.findAll(pageable);

        return ResponseEntity.ok(pagedAssembler.toModel(clientResponsePage, clientModelAssembler));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ClientResponse>> getClientById(@PathVariable Long id){
        ClientResponse clientResponse = clientService.findById(id);

        return ResponseEntity.ok(clientModelAssembler.toModel(clientResponse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClientById(@PathVariable Long id){
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ClientResponse> patchClient(@PathVariable Long id,@RequestBody ClientRequest client) {
        ClientResponse updatedClient = clientService.updateClientPartial(id, client);
        return ResponseEntity.ok(updatedClient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> putClient(@PathVariable Long id,@Valid @RequestBody ClientRequest client) {
        ClientResponse updatedClient = clientService.updateClientFull(id, client);
        return ResponseEntity.ok(updatedClient);
    }
}