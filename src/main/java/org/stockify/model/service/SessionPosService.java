package org.stockify.model.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.sessionpos.SessionPosRequest;
import org.stockify.dto.response.SessionPosCreateResponse;
import org.stockify.dto.response.SessionPosResponse;
import org.stockify.model.entity.SessionPosEntity;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.SessionPosMapper;
import org.stockify.model.repository.SessionPosRepository;
import org.stockify.model.specification.SessionPosSpecifications;

import java.math.BigDecimal;
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
     * Busca sesiones de POS aplicando una especificación.
     * @param spec La especificación a aplicar.
     * @param pageable Información de paginación.
     * @return Página de sesiones de POS que cumplen con la especificación.
     */
    public Page<SessionPosResponse> findAll(Specification<SessionPosEntity> spec, Pageable pageable) {
        Page<SessionPosEntity> sessions = repository.findAll(spec, pageable);
        return sessions.map(sessionPosMapper::toDto);
    }

    /**
     * Busca sesiones de POS aplicando filtros.
     * @param employeeId ID del empleado (opcional).
     * @param posId ID del POS (opcional).
     * @param openingTimeStart Fecha de inicio para filtrar por fecha de apertura (opcional).
     * @param openingTimeEnd Fecha de fin para filtrar por fecha de apertura (opcional).
     * @param closeTimeStart Fecha de inicio para filtrar por fecha de cierre (opcional).
     * @param closeTimeEnd Fecha de fin para filtrar por fecha de cierre (opcional).
     * @param openingAmountMin Monto mínimo de apertura (opcional).
     * @param openingAmountMax Monto máximo de apertura (opcional).
     * @param closeAmountMin Monto mínimo de cierre (opcional).
     * @param closeAmountMax Monto máximo de cierre (opcional).
     * @param isOpen Si es true, filtra solo sesiones abiertas; si es false, filtra solo sesiones cerradas; si es null, no filtra por estado (opcional).
     * @param pageable Información de paginación.
     * @return Página de sesiones de POS que cumplen con los filtros.
     */
    public Page<SessionPosResponse> findAllWithFilters(
            Long employeeId,
            Long posId,
            LocalDateTime openingTimeStart,
            LocalDateTime openingTimeEnd,
            LocalDateTime closeTimeStart,
            LocalDateTime closeTimeEnd,
            BigDecimal openingAmountMin,
            BigDecimal openingAmountMax,
            BigDecimal closeAmountMin,
            BigDecimal closeAmountMax,
            Boolean isOpen,
            Pageable pageable) {

        Specification<SessionPosEntity> spec = Specification.where(null);

        if (employeeId != null) {
            spec = spec.and(SessionPosSpecifications.hasEmployeeId(employeeId));
        }

        if (posId != null) {
            spec = spec.and(SessionPosSpecifications.hasPosId(posId));
        }

        if (openingTimeStart != null) {
            spec = spec.and(SessionPosSpecifications.hasOpeningTimeAfter(openingTimeStart));
        }

        if (openingTimeEnd != null) {
            spec = spec.and(SessionPosSpecifications.hasOpeningTimeBefore(openingTimeEnd));
        }

        if (closeTimeStart != null) {
            spec = spec.and(SessionPosSpecifications.hasCloseTimeAfter(closeTimeStart));
        }

        if (closeTimeEnd != null) {
            spec = spec.and(SessionPosSpecifications.hasCloseTimeBefore(closeTimeEnd));
        }

        if (openingAmountMin != null) {
            spec = spec.and(SessionPosSpecifications.hasOpeningAmountGreaterThan(openingAmountMin));
        }

        if (openingAmountMax != null) {
            spec = spec.and(SessionPosSpecifications.hasOpeningAmountLessThan(openingAmountMax));
        }

        if (closeAmountMin != null) {
            spec = spec.and(SessionPosSpecifications.hasCloseAmountGreaterThan(closeAmountMin));
        }

        if (closeAmountMax != null) {
            spec = spec.and(SessionPosSpecifications.hasCloseAmountLessThan(closeAmountMax));
        }

        if (isOpen != null) {
            if (isOpen) {
                spec = spec.and(SessionPosSpecifications.isOpen());
            } else {
                spec = spec.and(SessionPosSpecifications.isClosed());
            }
        }

        return findAll(spec, pageable);
    }
    
}
