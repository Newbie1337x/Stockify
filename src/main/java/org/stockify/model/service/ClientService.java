package org.stockify.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.client.ClientFilterRequest;
import org.stockify.dto.request.client.ClientRequest;
import org.stockify.dto.response.ClientResponse;
import org.stockify.model.entity.ClientEntity;
import org.stockify.model.exception.ClientNotFoundException;
import org.stockify.model.mapper.ClientMapper;
import org.stockify.model.repository.ClientRepository;
import org.stockify.model.specification.ClientSpecification;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Autowired
    public ClientService(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    public ClientResponse findById(Long id) {
        ClientEntity clientEntity = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client with id " + id + " not found"));
        return clientMapper.toDto(clientEntity);
    }

    public Page<ClientResponse> findAll(ClientFilterRequest filterRequest, Pageable pageable) {
        Specification<ClientEntity> specification = Specification
                .where(ClientSpecification.firstNameLike(filterRequest.getFirstName()))
                .and(ClientSpecification.lastNameLike(filterRequest.getLastName()))
                .and(ClientSpecification.dniEquals(filterRequest.getDni()))
                .and(ClientSpecification.phoneLike(filterRequest.getPhone()));

        Page<ClientEntity> clients = clientRepository.findAll(specification, pageable);
        return clients.map(clientMapper::toDto);
    }

    public ClientResponse save(ClientRequest clientRequest) {
        ClientEntity clientEntity = clientMapper.toEntity(clientRequest);
        return clientMapper.toDto(clientRepository.save(clientEntity));
    }

    public void delete(Long id) {
        if(!clientRepository.existsById(id)) {
            throw new ClientNotFoundException("Client with id " + id + " not found");
        }
        clientRepository.deleteById(id);
    }

    public ClientResponse updateClientPartial (Long id, ClientRequest clientRequest) {
        ClientEntity existingClient =
                clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client with id " + id + " not found"));

        clientMapper.partialUpdateClientEntity(clientRequest, existingClient);

        ClientEntity updatedClient = clientRepository.save(existingClient);
        return clientMapper.toDto(updatedClient);
    }

    public ClientResponse updateClientFull (Long id, ClientRequest clientRequest) {
        ClientEntity existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client with id " + id + " not found"));

        clientMapper.updateClientEntity(clientRequest, existingClient);

        ClientEntity updatedClient = clientRepository.save(existingClient);
        return clientMapper.toDto(updatedClient);
    }
}