package org.stockify.model.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.model.entities.ClientEntity;
import org.stockify.model.services.ClientService;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientServices;
    private final ClientService clientService;

    public ClientController(ClientService clientServices, ClientService clientService) {this.clientServices = clientServices;
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<Void> createClient(@RequestBody ClientEntity client) {
        System.out.println(client);
        clientService.save(client);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<ClientEntity>> getAllClients(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<ClientEntity> clientEntityPage = clientService.findAll(pageable);

        return ResponseEntity.ok(clientEntityPage);}
}
