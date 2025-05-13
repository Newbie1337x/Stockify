package org.stockify.model.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.stockify.model.entities.ClientEntity;
import org.stockify.model.exceptions.ClientNotFoundException;
import org.stockify.model.repositories.ClientRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    public ClientService(ClientRepository clientRepository) {this.clientRepository = clientRepository;}

    public ClientEntity findById(Long id) {return clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException("Client with id " + id + " not found"));}
    public Page<ClientEntity> findAll(Pageable pageable) {return clientRepository.findAll(pageable);}
    public void save(ClientEntity clientEntity) {clientRepository.save(clientEntity);}
    public void delete(Long id) {clientRepository.deleteById(id);}
}