package org.stockify.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.employee.TimeLogRequest;
import org.stockify.dto.response.TimeLogResponse;
import org.stockify.model.entity.TimeLogEntity;
import org.stockify.model.mapper.TimeLogMapper;
import org.stockify.model.repository.TimeLogRepository;
import org.stockify.model.specification.TimeLogSpecifications;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class TimeLogService {

    private final TimeLogRepository timeLogRepository;
    private final TimeLogMapper timeLogMapper;

    /**
     * Crea un nuevo registro de horario (TimeLog) para un empleado.
     *
     * @param timeLogRequest DTO con los datos del registro a crear
     * @return Entidad persistida con ID generado
     */
    public TimeLogEntity createTimeLog(TimeLogRequest timeLogRequest) {
        TimeLogEntity timeLogEntity = timeLogMapper.toEntity(timeLogRequest);
        return timeLogRepository.save(timeLogEntity);
    }

    /**
     * Obtiene una lista paginada de registros de horarios, con filtros opcionales por empleado, fecha y horarios.
     *
     * @param employeeId ID del empleado (obligatorio)
     * @param date Fecha en formato ISO (opcional)
     * @param clockInTime Hora de entrada en formato HH:mm:ss (opcional)
     * @param clockOutTime Hora de salida en formato HH:mm:ss (opcional)
     * @param pageable Parámetros de paginación y ordenamiento
     * @return Página con los registros que coincidan con los filtros
     */
    public Page<TimeLogResponse> getTimeLogs(
            Long employeeId, String date, String clockInTime, String clockOutTime, Pageable pageable
    ) {
        LocalDate parsedDate = (date != null) ? LocalDate.parse(date) : null;
        LocalTime inTime = (clockInTime != null) ? LocalTime.parse(clockInTime) : null;
        LocalTime outTime = (clockOutTime != null) ? LocalTime.parse(clockOutTime) : null;

        Specification<TimeLogEntity> spec = Specification
                .where(TimeLogSpecifications.hasEmployeeId(employeeId))
                .and(TimeLogSpecifications.hasDate(parsedDate))
                .and(TimeLogSpecifications.hasClockInTime(inTime))
                .and(TimeLogSpecifications.hasClockOutTime(outTime));

        return timeLogRepository.findAll(spec, pageable).map(timeLogMapper::toResponse);
    }

    /**
     * Obtiene un registro de horario por su ID.
     *
     * @param id ID del registro a buscar
     * @return DTO con los datos del registro encontrado
     * @throws NoSuchElementException si no se encuentra el ID
     */
    public TimeLogResponse getTimeLogById(long id) {
        return timeLogMapper.toResponse(timeLogRepository.findById(id).get());
    }

    /**
     * Actualiza un registro de horario existente por su ID.
     *
     * @param id ID del registro a actualizar
     * @param timeLogRequest DTO con los nuevos datos
     * @return DTO con el registro actualizado
     */
    public TimeLogResponse updateTimeLog(long id, TimeLogRequest timeLogRequest) {
        TimeLogEntity timeLogEntity = timeLogMapper.toEntity(timeLogRequest);
        timeLogEntity.setId(id);
        return timeLogMapper.toResponse(timeLogRepository.save(timeLogEntity));
    }

}
