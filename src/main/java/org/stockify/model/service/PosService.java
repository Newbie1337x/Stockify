package org.stockify.model.service;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.pos.PosAmountRequest;
import org.stockify.dto.request.pos.PosRequest;
import org.stockify.dto.request.sessionpos.SessionPosRequest;
import org.stockify.dto.response.PosResponse;
import org.stockify.model.entity.PosEntity;
import org.stockify.model.entity.SessionPosEntity;
import org.stockify.model.enums.Status;
import org.stockify.model.exception.InvalidSessionStatusException;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.PosMapper;
import org.stockify.model.repository.PosRepository;

import java.beans.JavaBean;
import java.time.LocalDateTime;
import java.util.List;

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

    @Deprecated
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
    /**
     * Abre una caja registradora (POS) si está en estado OFFLINE y no tiene una sesión abierta.
     * Busca el POS por su ID. Si no existe, lanza una excepción {@link NotFoundException}.
     * Luego verifica que el POS esté OFFLINE y que no tenga sesión abierta.
     * Si ambas condiciones se cumplen, guarda la sesión y devuelve {@code true}.
     * En caso contrario, lanza una excepción {@link InvalidSessionStatusException}.
     * @param id el ID del POS a abrir.
     * @param sessionPosRequest Objeto con los datos de la sesión a crear para el POS.
     * @return {@code true} si la sesión se abre correctamente.
     * @throws NotFoundException             Si no se encuentra el POS con el ID proporcionado.
     * @throws InvalidSessionStatusException Si el POS ya está abierto o no está en estado OFFLINE.
     */
   public Boolean openPos(Long id,SessionPosRequest sessionPosRequest)
   {
       PosEntity pos = posRepository.findById(id).orElseThrow(
               () -> new NotFoundException("This POS is not Found")
       );
       if (pos.getStatus() == Status.OFFLINE && !sessionPosService.isOpened(pos.getId(), null)) {
           pos.setStatus(Status.ONLINE);
           posRepository.save(pos);
           sessionPosService.save(sessionPosRequest);
           return true;
       }
       throw new InvalidSessionStatusException("This POS is already opened");
   }
    /**
     * Cierra una POS (punto de venta) si está en estado ONLINE y no tiene una sesión abierta.
     * @param idPos identificador de la POS.
     * @param idSession Id de la sesión que se desea registrar al cerrar.
     * @return true si la POS fue cerrada exitosamente.
     * @throws NotFoundException si no se encuentra la POS con el ID proporcionado.
     * @throws InvalidSessionStatusException si la POS ya está cerrada o tiene una sesión abierta.
     */
   public Boolean closePos(@NotNull Long idPos,Long idSession)
   {
       PosEntity pos = posRepository.findById(idPos).orElseThrow(
               () -> new NotFoundException("This POS is not Found")
       );
       SessionPosEntity sessionPosEntity = sessionPosService.findById(idSession);
       if (pos.getStatus() == Status.ONLINE && sessionPosService.isOpened(pos.getId(),null)){
           sessionPosEntity.setCloseTime(LocalDateTime.now());
           pos.setStatus(Status.OFFLINE);
           posRepository.save(pos);
           sessionPosService.save(sessionPosEntity);
           return true;
       }
       throw new InvalidSessionStatusException("This pos is already closed");
   }

}
