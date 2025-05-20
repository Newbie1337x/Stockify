package org.stockify.model.service;

import org.springframework.stereotype.Service;
import org.stockify.dto.request.sessionpos.SessionPosRequest;
import org.stockify.dto.response.SessionPosResponse;
import org.stockify.model.entity.SessionPosEntity;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.SessionPosMapper;
import org.stockify.model.repository.SessionPosRepository;

import java.time.LocalDateTime;

@Service
public class SessionPosService {

    private final SessionPosRepository repository;
    private final SessionPosMapper sessionPosMapper;

    public SessionPosService(SessionPosRepository repository, SessionPosMapper sessionPosMapper) {
        this.repository = repository;
        this.sessionPosMapper = sessionPosMapper;
    }


    /**
     * Guarda una nueva sesión POS en la base de datos a partir de un objeto de solicitud (DTO).
     *
     * <p>Este método convierte el DTO {@link SessionPosRequest} recibido en una entidad,
     * lo persiste usando el repositorio, y luego convierte la entidad persistida
     * en un DTO de respuesta {@link SessionPosResponse} que se retorna.</p>
     * @param sessionPosRequest el objeto que contiene los datos de la sesión a guardar.
     * @return un {@link SessionPosResponse} que representa la sesión guardada.
     */
    public SessionPosResponse save(SessionPosRequest sessionPosRequest) {
        SessionPosEntity sessionPosEntity = sessionPosMapper.toEntity(sessionPosRequest);
        return sessionPosMapper.toDto(repository.save(sessionPosEntity));
    }

    /**
     * Guarda directamente una entidad de sesión POS en la base de datos.
     * Este método recibe una entidad {@link SessionPosEntity} ya construida, la guarda
     * en la base de datos y retorna un DTO {@link SessionPosResponse} que representa la sesión guardada
     * @param sessionPosEntity la entidad de sesión POS que se desea guardar.
     * @return un {@link SessionPosResponse} que representa la sesión guardada.
     */
    public SessionPosResponse save(SessionPosEntity sessionPosEntity){
        return sessionPosMapper.toDto(repository.save(sessionPosEntity));
    }

    public SessionPosEntity findById(Long id)
    {
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }

    public SessionPosEntity findByIdPosAndCloseTime(Long id, LocalDateTime closeTime)
    {
        return repository.findByPosEntity_IdAndCloseTime(id,closeTime)
                .orElseThrow(NotFoundException::new);
    }

    public Boolean isOpened(Long id, LocalDateTime closeTime)
    {
        return repository.existsByPosEntity_IdAndCloseTime(id,closeTime);
    }





}
