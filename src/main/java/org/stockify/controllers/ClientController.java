package org.stockify.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.model.entities.ClientEntity;
import org.stockify.model.services.ClientService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientServices, ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<Void> createClient(@RequestBody ClientEntity client) {
        System.out.println(client);
        clientService.save(client);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<ClientEntity>> getAllClients(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("firstName").ascending());
        Page<ClientEntity> clientEntityPage = clientService.findAll(pageable);

        return ResponseEntity.ok(clientEntityPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientEntity> getClientById(@PathVariable Long id){
        return ResponseEntity.ok(clientService.findById(id));
    }
}


