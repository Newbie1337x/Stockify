package org.stockify.model.service;

import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.pos.PosAmountRequest;
import org.stockify.dto.request.pos.PosCreateRequest;
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
import org.stockify.model.mapper.EmployeeMapper;
import org.stockify.model.mapper.PosMapper;
import org.stockify.model.mapper.SessionPosMapper;
import org.stockify.model.repository.PosRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PosService {

    private final PosRepository posRepository;
    private final PosMapper posMapper;
    private final SessionPosService sessionPosService;
    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;
    private final SessionPosMapper sessionPosMapper;

    public PosService(PosRepository posRepository, PosMapper posMapper, SessionPosService sessionPosService, EmployeeService employeeService, EmployeeMapper employeeMapper, SessionPosMapper sessionPosMapper) {
        this.posRepository = posRepository;
        this.posMapper = posMapper;
        this.sessionPosService = sessionPosService;
        this.employeeService = employeeService;
        this.employeeMapper = employeeMapper;
        this.sessionPosMapper = sessionPosMapper;
    }

    public PosResponse save(PosCreateRequest posCreateRequest) {
        PosEntity posEntity = posMapper.toEntity(posCreateRequest);
        return posMapper.toDto(posRepository.save(posEntity));
    }

    public List<PosResponse> findAll() {
        List<PosEntity> posEntities = posRepository.findAll();
        return posEntities.stream().map(posMapper::toDto).toList();
    }

    public PosResponse findById(Long id) {
        PosEntity posEntity = posRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("POS with ID " + id + " not found"));
        return posMapper.toDto(posEntity);
    }

    public List<PosResponse> findByStatus(Status statusRequest) {
        return posRepository.findByStatus(statusRequest)
                .stream()
                .map(posMapper::toDto)
                .toList();
    }

    @Deprecated
    public Status toggleStatus(Long id) {
        PosEntity pos = posRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("POS not found: " + id));
        Status next = (pos.getStatus() == Status.ONLINE) ? Status.OFFLINE : Status.ONLINE;
        pos.setStatus(next);
        posRepository.save(pos);
        return next;
    }

    public void patchAmount(Long id, PosAmountRequest posAmountRequest) {
        posRepository.findById(id).ifPresent(posEntity -> {
            posEntity.setCurrentAmount(posAmountRequest.getCurrentAmount());
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

    // crear un dto especifico para cuando se abre al caja
        // Cambiar verificacion por un optional orelse throw
    if (!employeeService.existByDni(employeeDni)) {
      throw new EmployeeNotFoundException("Employee with DNI " + employeeDni + " was not found.");
    }

        PosEntity pos = posRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("POS with ID " + id + " was not found."));

        boolean isOffline = pos.getStatus() == Status.OFFLINE;
        boolean isSessionClosed = !sessionPosService.isOpened(pos.getId(), null);

        if (!isOffline || !isSessionClosed) {
            throw new InvalidSessionStatusException("The POS is already opened or not in OFFLINE status.");
        }

        EmployeeEntity employee = employeeService.getEmployeeEntityByDni(employeeDni);

        pos.setStatus(Status.ONLINE);
        pos.setEmployee(employee);
        pos.setCurrentAmount(sessionPosRequest.getOpeningAmount());

        SessionPosEntity session = sessionPosMapper.toEntity(sessionPosRequest);
        session.setOpeningTime(LocalDateTime.now());
        session.setEmployee(employee);
        session.setPosEntity(pos);
        posRepository.save(pos);
        SessionPosCreateResponse sessionPosCreateResponse = sessionPosService.save(session);
        sessionPosCreateResponse.setIdPos(id);
        return sessionPosCreateResponse;
    }

    /**
     * Cierra una POS (punto de venta) si está en estado ONLINE y tiene una sesión abierta.
     * @param idPos identificador de la POS.
     * @param closeRequest datos de cierre, como el monto final.
     *
     * @return {@link SessionPosResponse} DTO con la información de la sesión cerrada.
     * @throws NotFoundException si no se encuentra la POS con el ID proporcionado.
     * @throws InvalidSessionStatusException si la POS ya está cerrada o no tiene una sesión abierta.
     */
    @Transactional
    public SessionPosResponse closePos(@NotNull Long idPos, @NotNull SessionPosCloseRequest closeRequest) {
        PosEntity pos = posRepository.findById(idPos)
                .orElseThrow(() -> new NotFoundException("This POS is not Found"));

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
