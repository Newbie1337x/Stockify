package org.stockify.model.service;

import lombok.RequiredArgsConstructor;
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

/**
 * Service class responsible for managing client-related operations,
 * including searching, saving, updating, and deleting clients.
 */
@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    /**
     * Finds a client by their ID.
     *
     * @param id the ID of the client to find
     * @return a DTO containing the client data
     * @throws ClientNotFoundException if no client is found with the specified ID
     */
    public ClientResponse findById(Long id) {
        ClientEntity clientEntity = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client with id " + id + " not found"));
        return clientMapper.toDto(clientEntity);
    }

    /**
     * Searches for clients matching the given filter criteria with pagination.
     *
     * @param filterRequest DTO containing filter criteria (first name, last name, DNI, phone)
     * @param pageable      pagination information
     * @return a paginated list of clients matching the filters
     */
    public Page<ClientResponse> findAll(ClientFilterRequest filterRequest, Pageable pageable) {
        Specification<ClientEntity> specification = Specification
                .where(ClientSpecification.firstNameLike(filterRequest.getFirstName()))
                .and(ClientSpecification.lastNameLike(filterRequest.getLastName()))
                .and(ClientSpecification.dniEquals(filterRequest.getDni()))
                .and(ClientSpecification.phoneLike(filterRequest.getPhone()));

        Page<ClientEntity> clients = clientRepository.findAll(specification, pageable);
        return clients.map(clientMapper::toDto);
    }

    /**
     * Saves a new client in the system.
     *
     * @param clientRequest DTO containing the client data to create
     * @return a DTO with the saved client data
     */
    public ClientResponse save(ClientRequest clientRequest) {
        ClientEntity clientEntity = clientMapper.toEntity(clientRequest);
        return clientMapper.toDto(clientRepository.save(clientEntity));
    }

    /**
     * Deletes a client by their ID.
     *
     * @param id the ID of the client to delete
     * @throws ClientNotFoundException if no client is found with the specified ID
     */
    public void delete(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ClientNotFoundException("Client with id " + id + " not found");
        }
        clientRepository.deleteById(id);
    }

    /**
     * Partially updates an existing client with the provided data.
     *
     * @param id            the ID of the client to update partially
     * @param clientRequest DTO containing the fields to update
     * @return a DTO with the updated client data
     * @throws ClientNotFoundException if no client is found with the specified ID
     */
    public ClientResponse updateClientPartial(Long id, ClientRequest clientRequest) {
        ClientEntity existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client with id " + id + " not found"));

        clientMapper.partialUpdateClientEntity(clientRequest, existingClient);

        ClientEntity updatedClient = clientRepository.save(existingClient);
        return clientMapper.toDto(updatedClient);
    }

    /**
     * Fully updates an existing client with the provided data.
     *
     * @param id            the ID of the client to update
     * @param clientRequest DTO containing the new client data
     * @return a DTO with the updated client data
     * @throws ClientNotFoundException if no client is found with the specified ID
     */
    public ClientResponse updateClientFull(Long id, ClientRequest clientRequest) {
        ClientEntity existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client with id " + id + " not found"));

        clientMapper.updateClientEntity(clientRequest, existingClient);

        ClientEntity updatedClient = clientRepository.save(existingClient);
        return clientMapper.toDto(updatedClient);
    }
}
