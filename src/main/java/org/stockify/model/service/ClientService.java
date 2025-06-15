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

@Service
@RequiredArgsConstructor

public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    /**
     * Busca un cliente por su ID.
     * 
     * @param id ID del cliente a buscar
     * @return DTO con los datos del cliente encontrado
     * @throws ClientNotFoundException si no se encuentra ningún cliente con el ID especificado
     */
    public ClientResponse findById(Long id) {
        ClientEntity clientEntity = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client with id " + id + " not found"));
        return clientMapper.toDto(clientEntity);
    }

    /**
     * Busca clientes aplicando filtros y paginación.
     * 
     * @param filterRequest DTO con los filtros a aplicar (nombre, apellido, DNI, teléfono)
     * @param pageable Información de paginación
     * @return Página de clientes que cumplen con los filtros
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
     * Guarda un nuevo cliente en el sistema.
     * 
     * @param clientRequest DTO con los datos del cliente a crear
     * @return DTO con los datos del cliente creado
     */
    public ClientResponse save(ClientRequest clientRequest) {
        ClientEntity clientEntity = clientMapper.toEntity(clientRequest);
        return clientMapper.toDto(clientRepository.save(clientEntity));
    }

    /**
     * Elimina un cliente por su ID.
     * 
     * @param id ID del cliente a eliminar
     * @throws ClientNotFoundException si no se encuentra ningún cliente con el ID especificado
     */
    public void delete(Long id) {
        if(!clientRepository.existsById(id)) {
            throw new ClientNotFoundException("Client with id " + id + " not found");
        }
        clientRepository.deleteById(id);
    }

    /**
     * Actualiza parcialmente un cliente existente.
     * 
     * @param id ID del cliente a actualizar parcialmente
     * @param clientRequest DTO con los datos a actualizar del cliente
     * @return DTO con los datos del cliente actualizado
     * @throws ClientNotFoundException si no se encuentra ningún cliente con el ID especificado
     */
    public ClientResponse updateClientPartial (Long id, ClientRequest clientRequest) {
        ClientEntity existingClient =
                clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client with id " + id + " not found"));

        clientMapper.partialUpdateClientEntity(clientRequest, existingClient);

        ClientEntity updatedClient = clientRepository.save(existingClient);
        return clientMapper.toDto(updatedClient);
    }

    /**
     * Actualiza completamente un cliente existente.
     * 
     * @param id ID del cliente a actualizar
     * @param clientRequest DTO con los nuevos datos del cliente
     * @return DTO con los datos del cliente actualizado
     * @throws ClientNotFoundException si no se encuentra ningún cliente con el ID especificado
     */
    public ClientResponse updateClientFull (Long id, ClientRequest clientRequest) {
        ClientEntity existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client with id " + id + " not found"));

        clientMapper.updateClientEntity(clientRequest, existingClient);

        ClientEntity updatedClient = clientRepository.save(existingClient);
        return clientMapper.toDto(updatedClient);
    }
}
