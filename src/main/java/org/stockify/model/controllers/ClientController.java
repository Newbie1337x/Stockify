package org.stockify.model.controllers;

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
    public ResponseEntity<List<ClientEntity>> getAllClients() {return ResponseEntity.ok(clientServices.findAll());}
}
