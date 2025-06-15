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


    public Page<StoreResponse> findAll(Pageable pageable) {
        Page<StoreEntity> stores = storeRepository.findAll(pageable);
        return stores.map(storeMapper::toResponse);
    }

    public StoreResponse findById(Long id) {
        StoreEntity store = getStoreById(id);
        return storeMapper.toResponse(store);
    }

    public StoreResponse save(StoreRequest request) {
        StoreEntity store = storeMapper.toEntity(request);
        store = storeRepository.save(store);
        return storeMapper.toResponse(store);
    }

    public StoreResponse update(Long id, StoreRequest request) {
        StoreEntity store = getStoreById(id);
        storeMapper.updateEntityFromRequest(request, store);
        store = storeRepository.save(store);
        return storeMapper.toResponse(store);
    }

    public StoreResponse patch(Long id, StoreRequest request) {
        StoreEntity store = getStoreById(id);
        storeMapper.patchEntityFromRequest(request, store);
    return storeMapper.toResponse(storeRepository.save(store));
    }

    public void deleteById(Long id) {
        storeRepository.deleteById(id);
    }

    public StoreEntity getStoreById(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Store not found with id: " + id));
    }
}
