package org.stockify.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.sessionpos.SessionPosFiltersRequest;
import org.stockify.dto.request.sessionpos.SessionPosRequest;
import org.stockify.dto.response.SessionPosCreateResponse;
import org.stockify.dto.response.SessionPosResponse;
import org.stockify.model.entity.SessionPosEntity;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.SessionPosMapper;
import org.stockify.model.repository.SessionPosRepository;
import org.stockify.model.specification.SessionPosSpecifications;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class responsible for handling business logic related to POS sessions.
 */
@Service
@RequiredArgsConstructor
public class SessionPosService {

    private final SessionPosRepository repository;
    private final SessionPosMapper sessionPosMapper;

    /**
     * Saves a new POS session based on the provided request DTO.
     * Converts the {@link SessionPosRequest} into an entity, persists it using the repository,
     * and then maps the saved entity to a {@link SessionPosResponse} to return.
     *
     * @param sessionPosRequest the request containing the session data to be saved.
     * @return a {@link SessionPosResponse} representing the saved session.
     */
    public SessionPosResponse save(SessionPosRequest sessionPosRequest) {
        SessionPosEntity sessionPosEntity = sessionPosMapper.toEntity(sessionPosRequest);
        return sessionPosMapper.toDto(repository.save(sessionPosEntity));
    }

    /**
     * Saves a POS session directly using a pre-constructed entity.
     * This method persists the provided {@link SessionPosEntity} and returns
     * a {@link SessionPosCreateResponse} representing the saved session.
     *
     * @param sessionPosEntity the session entity to be saved.
     * @return a {@link SessionPosCreateResponse} representing the saved session.
     */
    public SessionPosCreateResponse save(SessionPosEntity sessionPosEntity){
        return sessionPosMapper.toDtoCreate(repository.save(sessionPosEntity));
    }

    /**
     * Updates an existing POS session entity in the database.
     *
     * @param sessionPosEntity the updated session entity.
     * @return a {@link SessionPosResponse} representing the updated session.
     */
    public SessionPosResponse update(SessionPosEntity sessionPosEntity) {
        return sessionPosMapper.toDto(repository.save(sessionPosEntity));
    }

    /**
     * Finds a POS session by its ID.
     *
     * @param id the ID of the session.
     * @return a {@link SessionPosResponse} representing the found session.
     * @throws NotFoundException if the session does not exist.
     */
    public SessionPosResponse findById(Long id) {
        SessionPosEntity entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("SessionPos with ID " + id + " not found."));
        return sessionPosMapper.toDto(entity);
    }

    /**
     * Finds a POS session by POS ID and closing time.
     *
     * @param id        the ID of the POS.
     * @param closeTime the closing time of the session.
     * @return the matching {@link SessionPosEntity}.
     * @throws NotFoundException if no session matches the given criteria.
     */
    public SessionPosEntity findByIdPosAndCloseTime(Long id, LocalDateTime closeTime) {
        return repository.findByPosEntity_IdAndCloseTime(id, closeTime)
                .orElseThrow(() -> new NotFoundException("SessionPos with ID " + id + " not found."));
    }

    /**
     * Checks if a session is currently open based on POS ID and closing time.
     *
     * @param id        the POS ID.
     * @param closeTime the closing time.
     * @return true if the session is still open, false otherwise.
     */
    public Boolean isOpened(Long id, LocalDateTime closeTime) {
        return repository.existsByPosEntity_IdAndCloseTime(id, closeTime);
    }

    /**
     * Retrieves all POS sessions.
     *
     * @return a list of {@link SessionPosResponse}.
     */
    public List<SessionPosResponse> findAll() {
        List<SessionPosEntity> sessions = repository.findAll();
        return sessions.stream()
                .map(sessionPosMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all POS sessions with pagination support.
     *
     * @param pageable pagination information.
     * @return a paginated list of {@link SessionPosResponse}.
     */
    public Page<SessionPosResponse> findAll(Pageable pageable) {
        Page<SessionPosEntity> sessions = repository.findAll(pageable);
        return sessions.map(sessionPosMapper::toDto);
    }

    /**
     * Retrieves all POS sessions matching a specific {@link Specification}, with pagination.
     *
     * @param spec     the specification to apply.
     * @param pageable pagination information.
     * @return a paginated list of {@link SessionPosResponse} matching the specification.
     */
    public Page<SessionPosResponse> findAll(Specification<SessionPosEntity> spec, Pageable pageable) {
        Page<SessionPosEntity> sessions = repository.findAll(spec, pageable);
        return sessions.map(sessionPosMapper::toDto);
    }

    /**
     * Retrieves POS sessions based on multiple filter criteria such as:
     * <ul>
     *     <li>Employee ID</li>
     *     <li>POS ID</li>
     *     <li>Opening and closing time ranges</li>
     *     <li>Opening and closing amount ranges</li>
     *     <li>Cash difference limits</li>
     *     <li>Whether the session is currently open</li>
     * </ul>
     *
     * @param filtersRequest filter criteria provided in {@link SessionPosFiltersRequest}.
     * @param pageable       pagination information.
     * @return a paginated list of {@link SessionPosResponse} matching the filters.
     */
    public Page<SessionPosResponse> findAllWithFilters(SessionPosFiltersRequest filtersRequest, Pageable pageable) {
        Specification<SessionPosEntity> spec = Specification.where(SessionPosSpecifications.hasPosId(filtersRequest.getPosId()))
                .and(SessionPosSpecifications.hasEmployeeId(filtersRequest.getEmployeeId()))
                .and(SessionPosSpecifications.hasOpeningTimeAfter(filtersRequest.getOpeningTimeStart()))
                .and(SessionPosSpecifications.hasOpeningTimeBefore(filtersRequest.getOpeningTimeEnd()))
                .and(SessionPosSpecifications.hasCloseTimeAfter(filtersRequest.getCloseTimeStart()))
                .and(SessionPosSpecifications.hasCloseTimeBefore(filtersRequest.getCloseTimeEnd()))
                .and(SessionPosSpecifications.hasOpeningAmountGreaterThan(filtersRequest.getOpeningAmountMin()))
                .and(SessionPosSpecifications.hasOpeningAmountLessThan(filtersRequest.getOpeningAmountMax()))
                .and(SessionPosSpecifications.hasCloseAmountGreaterThan(filtersRequest.getCloseAmountMin()))
                .and(SessionPosSpecifications.hasCloseAmountLessThan(filtersRequest.getCloseAmountMax()))
                .and(SessionPosSpecifications.hasCashDifferenceGreaterThan(filtersRequest.getCashDifference()))
                .and(SessionPosSpecifications.hasCashDifferenceLessThan(filtersRequest.getCashDifference()))
                .and(SessionPosSpecifications.isOpen(filtersRequest.getIsOpen()));
        return findAll(spec, pageable);
    }
}
