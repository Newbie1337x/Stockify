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

@Service
@RequiredArgsConstructor

public class StoreService {
    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;


    /**
     * Finds all stores with pagination.
     *
     * @param pageable the pagination information
     * @return a paginated list of store responses
     */
    public Page<StoreResponse> findAll(Pageable pageable) {
        Page<StoreEntity> stores = storeRepository.findAll(pageable);
        return stores.map(storeMapper::toResponse);
    }

    /**
     * Finds a store by its ID.
     *
     * @param id the ID of the store
     * @return the store response
     * @throws NotFoundException if the store is not found
     */
    public StoreResponse findById(Long id) {
        StoreEntity store = getStoreById(id);
        return storeMapper.toResponse(store);
    }

    /**
     * Saves a new store.
     *
     * @param request the store request containing store details
     * @return the saved store response
     */
    public StoreResponse save(StoreRequest request) {
        StoreEntity store = storeMapper.toEntity(request);
        store = storeRepository.save(store);
        return storeMapper.toResponse(store);
    }

    /**
     * Updates an existing store by its ID.
     *
     * @param id      the ID of the store to update
     * @param request the store request containing updated details
     * @return the updated store response
     * @throws NotFoundException if the store is not found
     */
    public StoreResponse update(Long id, StoreRequest request) {
        StoreEntity store = getStoreById(id);
        storeMapper.updateEntityFromRequest(request, store);
        store = storeRepository.save(store);
        return storeMapper.toResponse(store);
    }

    /**
     * Patches an existing store by its ID.
     *
     * @param id      the ID of the store to patch
     * @param request the store request containing partial updates
     * @return the patched store response
     * @throws NotFoundException if the store is not found
     */
    public StoreResponse patch(Long id, StoreRequest request) {
        StoreEntity store = getStoreById(id);
        storeMapper.patchEntityFromRequest(request, store);
    return storeMapper.toResponse(storeRepository.save(store));
    }

    /**
     * Deletes a store by its ID.
     *
     * @param id the ID of the store to delete
     * @throws NotFoundException if the store is not found
     */
    public void deleteById(Long id) {
        storeRepository.deleteById(id);
    }

    /**
     * Retrieves a store by its ID, throwing an exception if not found.
     *
     * @param id the ID of the store
     * @return the store entity
     * @throws NotFoundException if the store is not found
     */
    public StoreEntity getStoreById(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Store not found with id: " + id));
    }
}
