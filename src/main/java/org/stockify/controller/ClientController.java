package org.stockify.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
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
public class ClientController {
    private final ClientService clientService;
    private final ClientModelAssembler clientModelAssembler;


    @PostMapping
    public ResponseEntity<EntityModel<ClientResponse>> createClient(@Valid @RequestBody ClientRequest client) {
        ClientResponse clientResponse = clientService.save(client);
        EntityModel<ClientResponse> entityModel = clientModelAssembler.toModel(clientResponse);
        return ResponseEntity
                .created(URI.create(entityModel.getRequiredLink("self").getHref()))
                .body(entityModel);
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<ClientResponse>>> getAllClients(
            @ModelAttribute ClientFilterRequest filterRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            PagedResourcesAssembler<ClientResponse> pagedAssembler) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("firstName").ascending());
        Page<ClientResponse> clientResponsePage = clientService.findAll(filterRequest, pageable);

        return ResponseEntity.ok(pagedAssembler.toModel(clientResponsePage, clientModelAssembler));
    }

    @GetMapping("/{clientID}")
    public ResponseEntity<EntityModel<ClientResponse>> getClientById(@PathVariable Long clientID){
        ClientResponse clientResponse = clientService.findById(clientID);

        return ResponseEntity.ok(clientModelAssembler.toModel(clientResponse));
    }

    @DeleteMapping("/{clientID}")
    public ResponseEntity<Void> deleteClientById(@PathVariable Long clientID){
        clientService.delete(clientID);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{clientID}")
    public ResponseEntity<EntityModel<ClientResponse>> patchClient(@PathVariable Long clientID,@RequestBody ClientRequest client) {
        ClientResponse updatedClient = clientService.updateClientPartial(clientID, client);
        EntityModel<ClientResponse> entityModel = clientModelAssembler.toModel(updatedClient);
        return ResponseEntity.ok(entityModel);
    }

    @PutMapping("/{clientID}")
    public ResponseEntity<EntityModel<ClientResponse>> putClient(@PathVariable Long clientID,@Valid @RequestBody ClientRequest client) {
        ClientResponse updatedClient = clientService.updateClientFull(clientID, client);
        EntityModel<ClientResponse> entityModel = clientModelAssembler.toModel(updatedClient);
        return ResponseEntity.ok(entityModel);
    }
}