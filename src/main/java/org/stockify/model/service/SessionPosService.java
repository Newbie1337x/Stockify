package org.stockify.model.service;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.sessionpos.SessionPosRequest;
import org.stockify.dto.response.SessionPosResponse;
import org.stockify.model.entity.SessionPosEntity;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.PosMapper;
import org.stockify.model.mapper.SessionPosMapper;
import org.stockify.model.repository.SessionPosRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SessionPosService {

    private final SessionPosRepository repository;
    private final SessionPosMapper sessionPosMapper;

    public SessionPosService(SessionPosRepository repository, SessionPosMapper sessionPosMapper) {
        this.repository = repository;
        this.sessionPosMapper = sessionPosMapper;
    }

    public SessionPosResponse save(SessionPosRequest sessionPosRequest) {
        SessionPosEntity sessionPosEntity = sessionPosMapper.toEntity(sessionPosRequest);
        return sessionPosMapper.toDto(repository.save(sessionPosEntity));
    }

    public SessionPosEntity findByIdAndCloseTime(Long id, LocalDateTime closeTime)
    {
        return repository.findByPosEntity_IdAndCloseTime(id,closeTime)
                .orElseThrow(NotFoundException::new);
    }

    public Boolean isOpened(Long id, LocalDateTime closeTime)
    {
        return repository.existsByPosEntity_IdAndCloseTime(id,closeTime);
    }





}
