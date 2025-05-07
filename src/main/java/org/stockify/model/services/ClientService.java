package org.stockify.model.services;

import org.springframework.stereotype.Service;
import org.stockify.model.entities.ClientEntity;
import org.stockify.model.repositories.ClientRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    public ClientService(ClientRepository clientRepository) {this.clientRepository = clientRepository;}

    public List<ClientEntity> findAll() {return clientRepository.findAll();}
    public void save(ClientEntity clientEntity) {clientRepository.save(clientEntity);}
}
