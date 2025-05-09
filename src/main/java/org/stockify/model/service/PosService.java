package org.stockify.model.service;

import org.springframework.stereotype.Service;
import org.stockify.model.dto.request.PosAmountRequest;
import org.stockify.model.dto.request.PosRequest;
import org.stockify.model.dto.response.PosResponse;
import org.stockify.model.entity.PosEntity;
import org.stockify.model.enums.Status;
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

    public List<PosResponse> findById(Long id)
    {
        return posRepository.findById(id)
                .stream()
                .map(posMapper::toDto)
                .toList();
    }

   public void patchStatus(Long id, Status status)
   {
       posRepository.findById(id).ifPresent(posEntity -> {
           posEntity.setStatus(status);
           posRepository.save(posEntity);
       });
   }

   public void patchAmount(Long id, PosAmountRequest posAmountRequest)
   {
       posRepository.findById(id).ifPresent(posEntity -> {
           posEntity.setCurrentAmount(posAmountRequest.getCurrentAmount());
           posRepository.save(posEntity);
       });
   }


}
