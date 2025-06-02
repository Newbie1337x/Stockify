package org.stockify.model.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.sessionpos.SessionPosRequest;
import org.stockify.dto.response.SessionPosCreateResponse;
import org.stockify.dto.response.SessionPosResponse;
import org.stockify.model.entity.SessionPosEntity;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.SessionPosMapper;
import org.stockify.model.repository.SessionPosRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
     * Este méthod convierte el DTO {@link SessionPosRequest} recibido en una entidad,
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
     * Este method recibe una entidad {@link SessionPosEntity} ya construida, la guarda
     * en la base de datos y retorna un DTO {@link org.stockify.dto.response.SessionPosCreateResponse} que representa la sesión guardada
     * @param sessionPosEntity la entidad de sesión POS que se desea guardar.
     * @return un {@link SessionPosCreateResponse} que representa la sesión guardada.
     */
    public SessionPosCreateResponse save(SessionPosEntity sessionPosEntity){
        return sessionPosMapper.toDtoCreate(repository.save(sessionPosEntity));
    }

    public SessionPosResponse update(SessionPosEntity sessionPosEntity)
    {
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

    /**
     * Recupera todas las sesiones de POS.
     * @return Lista de todas las sesiones de POS.
     */
    public List<SessionPosResponse> findAll() {
        List<SessionPosEntity> sessions = repository.findAll();
        return sessions.stream()
                .map(sessionPosMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Recupera todas las sesiones de POS con paginación.
     * @param pageable Información de paginación.
     * @return Página de sesiones de POS.
     */
    public Page<SessionPosResponse> findAll(Pageable pageable) {
        Page<SessionPosEntity> sessions = repository.findAll(pageable);
        return sessions.map(sessionPosMapper::toDto);
    }

    /**
     * Convierte una entidad SessionPosEntity a un DTO SessionPosResponse.
     * @param sessionPosEntity La entidad a convertir.
     * @return El DTO resultante.
     */
    public SessionPosResponse entityToDto(SessionPosEntity sessionPosEntity) {
        return sessionPosMapper.toDto(sessionPosEntity);
    }





}
