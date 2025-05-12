package org.stockify.model.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.stockify.model.entities.ClientEntity;
import org.stockify.model.repositories.ClientRepository;

import java.util.List;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    public ClientService(ClientRepository clientRepository) {this.clientRepository = clientRepository;}

    public Page<ClientEntity> findAll(Pageable pageable) {return clientRepository.findAll();}
    public void save(ClientEntity clientEntity) {clientRepository.save(clientEntity);}
}
