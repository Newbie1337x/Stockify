package org.stockify.model.service;

import org.springframework.stereotype.Service;
import org.stockify.dto.request.pos.PosAmountRequest;
import org.stockify.dto.request.pos.PosRequest;
import org.stockify.dto.response.PosResponse;
import org.stockify.model.entity.PosEntity;
import org.stockify.model.enums.Status;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.PosMapper;
import org.stockify.model.repository.PosRepository;

import java.util.List;

@Service
public class PosService {

    private final PosRepository posRepository;
    private final PosMapper posMapper;

    public PosService(PosRepository posRepository, PosMapper posMapper) {
        this.posRepository = posRepository;
        this.posMapper = posMapper;
    }

    public PosResponse save(PosRequest posRequest)
    {
        PosEntity posEntity = posMapper.toEntity(posRequest);
        return posMapper.toDto(posRepository.save(posEntity));
    }

    public List<PosResponse> findAll()
    {
        List<PosEntity> posEntities = posRepository.findAll();
        return posEntities.stream().map(posMapper::toDto).toList();
    }

    public PosResponse findById(Long id)
    {
        PosEntity posEntity = posRepository
                .findById(id)
                .orElseThrow
                        (() -> new NotFoundException("POS with ID " + id + " not found"));
        return posMapper.toDto(posEntity);
    }
    public List<PosResponse> findByStatus(Status statusRequest) {
        return posRepository.findByStatus(statusRequest)
                .stream()
                .map(posMapper::toDto)
                .toList();
    }


    public Status toggleStatus(Long id) {
        // Recupera o lanza excepción si no existe
        PosEntity pos = posRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("POS not found: " + id));
        Status next = (pos.getStatus() == Status.ONLINE)
                ? Status.OFFLINE
                : Status.ONLINE;
        pos.setStatus(next);
        posRepository.save(pos);
        return next;
    }

    public void patchAmount(Long id, PosAmountRequest posAmountRequest)
   {
       posRepository.findById(id).ifPresent(posEntity -> {
           posEntity.setCurrentAmount(posAmountRequest.getCurrentAmount());
           posRepository.save(posEntity);
       });
   }

}
