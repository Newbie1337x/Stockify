package org.stockify.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.ClientRequest;
import org.stockify.dto.response.ClientResponse;
import org.stockify.model.services.ClientService;

@RestController
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<ClientResponse> createClient(@RequestBody ClientRequest client) {
        ClientResponse clientResponse = clientService.save(client);

        return new ResponseEntity<>(clientResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<ClientResponse>> getAllClients(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("firstName").ascending());
        Page<ClientResponse> clientResponsePage = clientService.findAll(pageable);

        return ResponseEntity.ok(clientResponsePage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClientById(@PathVariable Long id){
        ClientResponse clientResponse = clientService.findById(id);

        return ResponseEntity.ok(clientResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClientById(@RequestParam Long id){
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }


    /// PUT Y PATCH A ENTIDAD O DTO ?  ? ?  ? ? ?
    @PatchMapping("/{id}")
    public ResponseEntity<ClientResponse> updateClient(@PathVariable Long id, @RequestBody ClientRequest client) {
        ClientResponse updatedClient = clientService.updateClientPartial(id, client);
        return ResponseEntity.ok(updatedClient);
    }



}


