package org.stockify.model.service;

import org.springframework.stereotype.Service;
import org.stockify.dto.request.sessionpos.SessionPosRequest;
import org.stockify.dto.response.SessionPosResponse;
import org.stockify.model.entity.SessionPosEntity;
import org.stockify.model.mapper.PosMapper;
import org.stockify.model.mapper.SessionPosMapper;
import org.stockify.model.repository.SessionPosRepository;

@Service
public class SessionPosService {

    private final SessionPosRepository repository;
    private final SessionPosMapper sessionPosMapper;

    public SessionPosService(SessionPosRepository repository, SessionPosMapper sessionPosMapper) {
        this.repository = repository;
        this.sessionPosMapper = sessionPosMapper;
    }

    private SessionPosResponse save(SessionPosRequest sessionPosRequest)
    {
        SessionPosEntity sessionPosEntity = sessionPosMapper.toEntity(sessionPosRequest);
        return sessionPosMapper.toDto(repository.save(sessionPosEntity));
    }

    


}
