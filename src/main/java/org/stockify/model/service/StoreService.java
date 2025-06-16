package org.stockify.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.store.StoreRequest;
import org.stockify.dto.response.StoreResponse;
import org.stockify.model.entity.StoreEntity;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.StoreMapper;
import org.stockify.model.repository.StoreRepository;

/**
 * Servicio para gestionar operaciones relacionadas con tiendas (stores).
 */
@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;

    /**
     * Obtiene todas las tiendas paginadas.
     *
     * @param pageable Parámetros de paginación y ordenamiento
     * @return Página de respuestas con las tiendas
     */
    public Page<StoreResponse> findAll(Pageable pageable) {
        Page<StoreEntity> stores = storeRepository.findAll(pageable);
        return stores.map(storeMapper::toResponse);
    }

    /**
     * Busca una tienda por su ID.
     *
     * @param id ID de la tienda a buscar
     * @return DTO con los datos de la tienda encontrada
     * @throws NotFoundException si la tienda no existe
     */
    public StoreResponse findById(Long id) {
        StoreEntity store = getStoreById(id);
        return storeMapper.toResponse(store);
    }

    /**
     * Guarda una nueva tienda en la base de datos.
     *
     * @param request DTO con los datos de la tienda a crear
     * @return DTO con los datos de la tienda creada
     */
    public StoreResponse save(StoreRequest request) {
        StoreEntity store = storeMapper.toEntity(request);
        store = storeRepository.save(store);
        return storeMapper.toResponse(store);
    }

    /**
     * Actualiza completamente una tienda existente.
     *
     * @param id ID de la tienda a actualizar
     * @param request DTO con los nuevos datos
     * @return DTO con los datos de la tienda actualizada
     * @throws NotFoundException si la tienda no existe
     */
    public StoreResponse update(Long id, StoreRequest request) {
        StoreEntity store = getStoreById(id);
        storeMapper.updateEntityFromRequest(request, store);
        store = storeRepository.save(store);
        return storeMapper.toResponse(store);
    }

    /**
     * Aplica una actualización parcial (patch) a una tienda.
     *
     * @param id ID de la tienda a modificar
     * @param request DTO con los datos parciales a modificar
     * @return DTO con los datos de la tienda actualizada
     * @throws NotFoundException si la tienda no existe
     */
    public StoreResponse patch(Long id, StoreRequest request) {
        StoreEntity store = getStoreById(id);
        storeMapper.patchEntityFromRequest(request, store);
        return storeMapper.toResponse(storeRepository.save(store));
    }

    /**
     * Elimina una tienda por su ID.
     *
     * @param id ID de la tienda a eliminar
     */
    public void deleteById(Long id) {
        storeRepository.deleteById(id);
    }

    /**
     * Obtiene una tienda por su ID o lanza una excepción si no existe.
     *
     * @param id ID de la tienda
     * @return Entidad de tienda
     * @throws NotFoundException si la tienda no existe
     */
    public StoreEntity getStoreById(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Store not found with id: " + id));
    }
}
