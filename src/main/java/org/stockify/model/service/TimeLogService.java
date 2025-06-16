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
import org.stockify.model.exception.NotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Service responsible for managing employee time log records.
 * Provides functionalities for creating, retrieving, updating, and filtering logs based on employee ID,
 * date, and clock-in/clock-out times.
 */
@RequiredArgsConstructor
@Service
public class TimeLogService {

    private final TimeLogRepository timeLogRepository;
    private final TimeLogMapper timeLogMapper;

    /**
     * Creates and saves a new time log entry for an employee.
     *
     * @param timeLogRequest the DTO containing the time log data to be saved
     * @return a {@link TimeLogResponse} representing the saved entry with a generated ID
     */
    public TimeLogResponse createTimeLog(TimeLogRequest timeLogRequest) {
        TimeLogEntity timeLogEntity = timeLogMapper.toEntity(timeLogRequest);
        return timeLogMapper.toResponse(timeLogRepository.save(timeLogEntity));
    }

    /**
     * Retrieves a paginated list of time logs, optionally filtered by date, clock-in time, and clock-out time.
     *
     * @param employeeId    the ID of the employee (required)
     * @param date          the date filter in ISO format (optional)
     * @param clockInTime   the clock-in time filter in HH:mm:ss format (optional)
     * @param clockOutTime  the clock-out time filter in HH:mm:ss format (optional)
     * @param pageable      pagination and sorting parameters
     * @return a {@link Page} of {@link TimeLogResponse} matching the given filters
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
     * Retrieves a single time log entry by its unique ID.
     *
     * @param id the ID of the time log to retrieve
     * @return a {@link TimeLogResponse} representing the found time log
     * @throws NotFoundException if no time log is found with the provided ID
     */
    public TimeLogResponse getTimeLogById(long id) {
        return timeLogMapper
                .toResponse(timeLogRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new NotFoundException("TimeLog not found with id: " + id)
                        )
                );
    }

    /**
     * Updates an existing time log entry by its ID.
     * The existing record will be overwritten with the values from the provided request.
     *
     * @param id             the ID of the time log to update
     * @param timeLogRequest the DTO containing the updated data
     * @return a {@link TimeLogResponse} representing the updated time log
     */
    public TimeLogResponse updateTimeLog(long id, TimeLogRequest timeLogRequest) {
        TimeLogEntity timeLogEntity = timeLogMapper.toEntity(timeLogRequest);
        timeLogEntity.setId(id);
        return timeLogMapper.toResponse(timeLogRepository.save(timeLogEntity));
    }

}
