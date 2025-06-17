package org.stockify.model.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.pos.PosAmountRequest;
import org.stockify.dto.request.pos.PosFilterRequest;
import org.stockify.dto.request.sessionpos.SessionPosCloseRequest;
import org.stockify.dto.request.sessionpos.SessionPosRequest;
import org.stockify.dto.response.PosResponse;
import org.stockify.dto.response.SessionPosCreateResponse;
import org.stockify.dto.response.SessionPosResponse;
import org.stockify.model.entity.EmployeeEntity;
import org.stockify.model.entity.PosEntity;
import org.stockify.model.entity.SessionPosEntity;
import org.stockify.model.enums.Status;
import org.stockify.model.exception.EmployeeNotFoundException;
import org.stockify.model.exception.InvalidSessionStatusException;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.PosMapper;
import org.stockify.model.mapper.SessionPosMapper;
import org.stockify.model.repository.PosRepository;
import org.stockify.model.repository.StoreRepository;
import org.stockify.model.specification.PosSpecification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class responsible for managing POS (Point of Sale) terminals.
 * <p>
 * Provides operations to create, retrieve, update, open, and close POS terminals,
 * as well as methods to query POS terminals with filtering and pagination.
 * It also handles business logic related to POS sessions and employee assignments.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class PosService {

    private final PosRepository posRepository;
    private final PosMapper posMapper;
    private final SessionPosService sessionPosService;
    private final EmployeeService employeeService;
    private final SessionPosMapper sessionPosMapper;
    private final StoreRepository storeRepository;

    /**
     * Creates and saves a new POS terminal associated with a store.
     *
     * @param idLocal ID of the store to associate with the POS terminal.
     * @return {@link PosResponse} DTO containing the data of the created POS terminal.
     * @throws NotFoundException if the store with the given ID does not exist.
     */
    public PosResponse save(Long idLocal) {
        PosEntity posEntity = PosEntity.builder()
                .currentAmount(BigDecimal.ZERO)
                .store(
                        storeRepository.findById(idLocal)
                                .orElseThrow(() -> new NotFoundException("Local with ID " + idLocal + " not found")))
                .status(Status.OFFLINE)
                .build();
        return posMapper.toDto(posRepository.save(posEntity));
    }

    /**
     * Retrieves all POS terminals without pagination.
     *
     * @return A list of {@link PosResponse} DTOs representing all POS terminals.
     */
    public List<PosResponse> findAll() {
        List<PosEntity> posEntities = posRepository.findAll();
        return posEntities.stream().map(posMapper::toDto).toList();
    }

    /**
     * Retrieves all POS terminals with pagination.
     *
     * @param pageable Pagination information (page number, size, sorting).
     * @return A page of {@link PosResponse} DTOs representing POS terminals.
     */
    public Page<PosResponse> findAll(Pageable pageable) {
        Page<PosEntity> posEntities = posRepository.findAll(pageable);
        return posEntities.map(posMapper::toDto);
    }

    /**
     * Retrieves all POS terminals matching the given specification with pagination.
     *
     * @param spec     Specification to filter the POS terminals.
     * @param pageable Pagination information.
     * @return A page of {@link PosResponse} DTOs representing filtered POS terminals.
     */
    public Page<PosResponse> findAll(Specification<PosEntity> spec, Pageable pageable) {
        Page<PosEntity> posEntities = posRepository.findAll(spec, pageable);
        return posEntities.map(posMapper::toDto);
    }

    /**
     * Finds a POS terminal by its ID.
     *
     * @param id ID of the POS terminal.
     * @return {@link PosResponse} DTO containing data of the found POS terminal.
     * @throws NotFoundException if no POS terminal with the given ID exists.
     */
    public PosResponse findById(Long id) {
        PosEntity posEntity = posRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("POS with ID " + id + " not found"));
        return posMapper.toDto(posEntity);
    }

    /**
     * Retrieves POS terminals filtered by their status with pagination.
     *
     * @param statusRequest Status to filter POS terminals.
     * @param pageable      Pagination information.
     * @return A page of {@link PosResponse} DTOs matching the status filter.
     */
    public Page<PosResponse> findByStatus(Status statusRequest, Pageable pageable) {
        Page<PosEntity> posEntities = posRepository.findByStatus(statusRequest, pageable);
        return posEntities.map(posMapper::toDto);
    }

    /**
     * Finds POS terminals applying multiple optional filters with pagination.
     *
     * Supported filters include:
     * <ul>
     *     <li>id: ID of the POS terminal (optional)</li>
     *     <li>storeId: Store ID (optional)</li>
     *     <li>status: POS terminal status (optional)</li>
     *     <li>employeeId: Employee ID associated with POS (optional)</li>
     *     <li>currentAmountMin: Minimum current amount (optional)</li>
     *     <li>currentAmountMax: Maximum current amount (optional)</li>
     * </ul>
     *
     * @param filters  DTO containing filter criteria.
     * @param pageable Pagination information.
     * @return A page of {@link PosResponse} DTOs matching the filter criteria.
     */
    public Page<PosResponse> findAllWithFilters(PosFilterRequest filters, Pageable pageable) {
        Specification<PosEntity> spec = Specification.where(PosSpecification.byId(filters.getId()))
                .and(PosSpecification.byStatus(filters.getStatus()))
                .and(PosSpecification.byEmployeeId(filters.getEmployeeId()))
                .and(PosSpecification.byStoreId(filters.getStoreId()))
                .and(PosSpecification.byCurrentAmountGreaterThan(filters.getCurrentAmount()))
                .and(PosSpecification.byCurrentAmountLessThan(filters.getCurrentAmount()));

        return findAll(spec, pageable);
    }

    /**
     * Sets the current amount of a POS terminal with the given ID.
     *
     * @param id               ID of the POS terminal.
     * @param posAmountRequest DTO containing the amount to set.
     */
    public void addAmount(Long id, PosAmountRequest posAmountRequest) {
        posRepository.findById(id).ifPresent(posEntity -> {
            posEntity.setCurrentAmount(posAmountRequest.getCurrentAmount());
            posRepository.save(posEntity);
        });
    }

    /**
     * Adds the specified total amount to the current amount of a POS terminal.
     *
     * @param id          ID of the POS terminal.
     * @param totalAmount Amount to add to the current amount.
     */
    public void addAmount(Long id, BigDecimal totalAmount) {
        posRepository.findById(id).ifPresent(posEntity -> {
            posEntity.setCurrentAmount(posEntity.getCurrentAmount().add(totalAmount));
            posRepository.save(posEntity);
        });
    }

    /**
     *
     * Opens a POS terminal if it is currently OFFLINE and no session is active.
     * <p>
     * This method verifies that the employee exists, the POS is offline, and that
     * no session is currently opened. Then it sets the POS status to ONLINE,
     * links the employee to the POS, and registers a new session with the opening time.
     * </p>
     *
     * @param id                ID of the POS terminal to open.
     * @param sessionPosRequest Request data to open the session, including employee DNI and opening amount.
     * @return {@link SessionPosCreateResponse} DTO containing data of the newly created session.
     * @throws NotFoundException              If the POS or employee is not found.
     * @throws InvalidSessionStatusException If the POS is not OFFLINE or already has an open session.
     */
    @Transactional
    public SessionPosCreateResponse openPos(Long id, SessionPosRequest sessionPosRequest) {

        String employeeDni = sessionPosRequest.getEmployeeDni();
        PosEntity pos = posRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("POS with ID " + id + " was not found."));

        boolean isOffline = pos.getStatus() == Status.OFFLINE;
        boolean isSessionClosed = !sessionPosService.isOpened(pos.getId(), null);

        if (!isOffline || !isSessionClosed) {
            throw new InvalidSessionStatusException("The POS is already opened or not in OFFLINE status.");
        }

        EmployeeEntity employee = employeeService
                .getEmployeeEntityByDni(employeeDni)
                .orElseThrow(() -> new NotFoundException("Employee with DNI " + employeeDni + " was not found."));

        pos.setStatus(Status.ONLINE);
        pos.setEmployee(employee);
        pos.setCurrentAmount(sessionPosRequest.getOpeningAmount());

        SessionPosEntity session = sessionPosMapper.toEntity(sessionPosRequest);
        session.setOpeningTime(LocalDateTime.now());
        session.setEmployee(employee);
        session.setPosEntity(pos);
        posRepository.save(pos);

        return sessionPosService.save(session);
    }

    /**
     *
     * Closes a POS terminal if it is currently ONLINE and has an open session.
     * Updates the session close time, close amount, expected amount,
     * and calculates cash difference. Also sets the POS status to OFFLINE,
     * unlinks the employee, and resets the current amount.
     *
     * @param idPos       ID of the POS terminal to close.
     * @param closeRequest Request data including the closing amount.
     * @return {@link SessionPosResponse} DTO with the updated session information.
     * @throws NotFoundException              If the POS terminal is not found.
     * @throws InvalidSessionStatusException If the POS is not ONLINE or has no open session.
     */
    @Transactional
    public SessionPosResponse closePos(@NotNull Long idPos, @NotNull SessionPosCloseRequest closeRequest) {
        PosEntity pos = posRepository.findById(idPos)
                .orElseThrow(() -> new NotFoundException("POS with ID " + idPos + " not found"));

        if (pos.getStatus() != Status.ONLINE || !sessionPosService.isOpened(idPos, null)) {
            throw new InvalidSessionStatusException("This POS is already closed");
        }

        SessionPosEntity session = sessionPosService.findByIdPosAndCloseTime(idPos, null);
        session.setCloseTime(LocalDateTime.now());
        session.setCloseAmount(closeRequest.getCloseAmount());
        session.setExpectedAmount(pos.getCurrentAmount());

        if (pos.getCurrentAmount() != null && closeRequest.getCloseAmount() != null) {
            session.setCashDifference(closeRequest.getCloseAmount().subtract(pos.getCurrentAmount()));
        }

        pos.setStatus(Status.OFFLINE);
        pos.setEmployee(null);
        pos.setCurrentAmount(BigDecimal.ZERO);
        posRepository.save(pos);

        return sessionPosService.update(session);
    }
}
