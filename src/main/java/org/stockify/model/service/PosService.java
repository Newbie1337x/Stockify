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
     * Guarda una nueva terminal POS (Point of Sale) asociada a un local.
     * @param idLocal ID del local al que se asociará la terminal POS.
     * @return {@link PosResponse} DTO con los datos de la terminal POS creada.
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
     * Busca todas las terminales POS (Point of Sale) disponibles.
     * @return {@link PosResponse} DTO con los datos de las terminales POS actualizadas.
     */
    public List<PosResponse> findAll() {
        List<PosEntity> posEntities = posRepository.findAll();
        return posEntities.stream().map(posMapper::toDto).toList();
    }


    /**
     * Busca todas las terminales POS (Point of Sale) con paginación.
     * @param pageable Información de paginación.
     * @return Página de {@link PosResponse} DTOs con los datos de las terminales POS.
     */
    public Page<PosResponse> findAll(Pageable pageable) {
        Page<PosEntity> posEntities = posRepository.findAll(pageable);
        return posEntities.map(posMapper::toDto);
    }

    /**
     * Busca todas las terminales POS (Point of Sale) aplicando una especificación.
     * @param spec Especificación para filtrar los resultados.
     * @param pageable Información de paginación.
     * @return Página de {@link PosResponse} DTOs con los datos de las terminales POS filtradas.
     */
    public Page<PosResponse> findAll(Specification<PosEntity> spec, Pageable pageable) {
        Page<PosEntity> posEntities = posRepository.findAll(spec, pageable);
        return posEntities.map(posMapper::toDto);
    }


    /**
     * Busca una terminal POS (Point of Sale) por su ID.
     * @param id ID de la terminal POS a buscar.
     * @return {@link PosResponse} DTO con los datos de la terminal POS encontrada.
     * @throws NotFoundException si no se encuentra la terminal POS con el ID proporcionado.
     */
    public PosResponse findById(Long id) {
        PosEntity posEntity = posRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("POS with ID " + id + " not found"));
        return posMapper.toDto(posEntity);
    }


    /**
     * Busca todas las terminales POS (Point of Sale) por su estado.
     * @param statusRequest Estado de la terminal POS a buscar.
     * @param pageable Información de paginación.
     * @return Página de {@link PosResponse} DTOs con los datos de las terminales POS encontradas.
     */
    public Page<PosResponse> findByStatus(Status statusRequest, Pageable pageable) {
        Page<PosEntity> posEntities = posRepository.findByStatus(statusRequest, pageable);
        return posEntities.map(posMapper::toDto);
    }

    /**
     * Busca POS aplicando filtros individuales.
     * id ID del POS (opcional).
     * storeId ID de la tienda (opcional).
     * status Estado del POS (opcional).
     * employeeId ID del empleado (opcional).
     * currentAmountMin Monto mínimo actual (opcional).
     * currentAmountMax Monto máximo actual (opcional).
     * @param pageable Información de paginación.
     * @return Página de POS que cumplen con los filtros.
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
     * Agrega una cantidad a la terminal POS (Point of Sale) especificada.
     * @param id
     * @param posAmountRequest
     */
    public void addAmount(Long id, PosAmountRequest posAmountRequest) {
        posRepository.findById(id).ifPresent(posEntity -> {
            posEntity.setCurrentAmount(posAmountRequest.getCurrentAmount());
            posRepository.save(posEntity);
        });
    }

    public void addAmount(Long id, BigDecimal totalAmount)
    {
        posRepository.findById(id).ifPresent(posEntity -> {
            posEntity.setCurrentAmount(posEntity.getCurrentAmount().add(totalAmount));
            posRepository.save(posEntity);
        });
    }

    /**
     * Abre una terminal POS (Point of Sale) si está disponible
     * Este méthod verifica que el empleado exista y que la terminal POS esté en estado OFFLINE
     * y sin una sesión abierta actualmente. Si esto es válido, cambia el estado de la POS a ONLINE,
     * vincula al empleado y registra una nueva sesión con la hora de apertura.
     *
     * @param id ID de la terminal POS a abrir.
     * @param sessionPosRequest Datos de la solicitud para abrir la sesión, incluyendo el DNI del empleado.
     * @return {@link PosResponse} DTO con los datos actualizados de la POS luego de abrirse.
     * @throws EmployeeNotFoundException Si el empleado no existe en el sistema.
     * @throws NotFoundException Si no se encuentra la POS con el ID dado.
     * @throws InvalidSessionStatusException Si la POS ya está abierta o no está en estado OFFLINE.
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
                .orElseThrow(
                        () -> new NotFoundException
                                ("Employee with DNI " + employeeDni + " was not found."));
        pos.setStatus(Status.ONLINE);
        pos.setEmployee(employee);
        pos.setCurrentAmount(sessionPosRequest.getOpeningAmount());

        SessionPosEntity session = sessionPosMapper.toEntity(sessionPosRequest);
        session.setOpeningTime(LocalDateTime.now());
        session.setEmployee(employee);
        session.setPosEntity(pos);
        posRepository.save(pos);
        SessionPosCreateResponse sessionPosCreateResponse = sessionPosService.save(session);
//        sessionPosCreateResponse.setIdPos(id);
        return sessionPosCreateResponse;
    }

    /**
     * Cierra una POS (punto de venta) si está en estado ONLINE y tiene una sesión abierta.
     * @param idPos identificador de la POS.
     * @param closeRequest datos de cierre, como el monto final
     *
     * @return {@link SessionPosResponse} DTO con la información de la sesión cerrada.
     * @throws NotFoundException si no se encuentra la POS con el ID proporcionado.
     * @throws InvalidSessionStatusException si la POS ya está cerrada o no tiene una sesión abierta.
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
