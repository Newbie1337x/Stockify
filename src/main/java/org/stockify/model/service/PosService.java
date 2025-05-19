package org.stockify.model.service;

import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.bytecode.enhance.spi.interceptor.SessionAssociableInterceptor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.pos.PosAmountRequest;
import org.stockify.dto.request.pos.PosRequest;
import org.stockify.dto.request.sessionpos.SessionPosRequest;
import org.stockify.dto.response.PosResponse;
import org.stockify.model.entity.PosEntity;
import org.stockify.model.enums.Status;
import org.stockify.model.exception.InvalidSessionStatusException;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.PosMapper;
import org.stockify.model.repository.PosRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PosService {

    private final PosRepository posRepository;
    private final PosMapper posMapper;
    private final SessionPosService sessionPosService;

    public PosService(PosRepository posRepository, PosMapper posMapper, SessionPosService sessionPosService) {
        this.posRepository = posRepository;
        this.posMapper = posMapper;
        this.sessionPosService = sessionPosService;
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

   public Boolean openPos(@NotNull PosRequest posRequest,SessionPosRequest sessionPosRequest)
   {
       PosEntity pos = posRepository.findById(posRequest.getIdCaja()).orElseThrow(
               () -> new NotFoundException("This POS is not Found")
       );
       if (pos.getStatus() == Status.OFFLINE && !sessionPosService.isOpened(pos.getId(), null)) {
           sessionPosService.save(sessionPosRequest);
           return true;
       }
       throw new InvalidSessionStatusException("This POS is already opened");
   }

}
