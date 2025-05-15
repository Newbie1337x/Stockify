package org.stockify.model.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.ClientRequest;
import org.stockify.dto.response.ClientResponse;
import org.stockify.model.entities.ClientEntity;
import org.stockify.model.exceptions.ClientNotFoundException;
import org.stockify.model.mapper.ClientMapper;
import org.stockify.model.repositories.ClientRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    public ClientService(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    public ClientResponse findById(Long id) {
        ClientEntity clientEntity = clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException("Client with id " + id + " not found"));
        return clientMapper.toDto(clientEntity);
    }

    public Page<ClientResponse> findAll(Pageable pageable) {
        Page<ClientEntity> clients = clientRepository.findAll(pageable);
        Page<ClientResponse> clientsDto = clients.map(clientMapper::toDto);

        return clientsDto;
    }

    public ClientResponse save(ClientRequest clientRequest) {
        ClientEntity clientEntity = clientMapper.toEnity(clientRequest);
        return clientMapper.toDto(clientRepository.save(clientEntity));
    }

    public void delete(Long id) {
        if(!clientRepository.existsById(id)) {
            throw new ClientNotFoundException("Client with id " + id + " not found");
        }
        clientRepository.deleteById(id);
    }

    public ClientResponse updateClientPartial (Long id, ClientRequest clientRequest) {
        ClientEntity existingClient = clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException("Client with id " + id + " not found"));

        if(clientRequest.getFirstName() != null) {
            existingClient.setFirstName(clientRequest.getFirstName());
        }
        if(clientRequest.getLastName() != null) {
            existingClient.setLastName(clientRequest.getLastName());
        }
        if(clientRequest.getPhone() != null) {
            existingClient.setPhone(clientRequest.getPhone());
        }
        if(clientRequest.getEmail() != null) {
            existingClient.setEmail(clientRequest.getEmail());
        }

        ClientEntity updatedClient = clientRepository.save(existingClient);
        return clientMapper.toDto(updatedClient);
    }
}