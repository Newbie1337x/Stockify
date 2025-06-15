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
@RequiredArgsConstructor

@Service
public class TimeLogService {

    private final TimeLogRepository timeLogRepository;
    private final TimeLogMapper timeLogMapper;

    /**
     * Creates a new time log entry.
     *
     * @param timeLogRequest the request containing time log details
     * @return the created TimeLogEntity
     */
    public TimeLogEntity createTimeLog(TimeLogRequest timeLogRequest) {
        System.out.println("---------------------------------------------------");
        System.out.println(timeLogRequest);
        TimeLogEntity timeLogEntity = timeLogMapper.toEntity(timeLogRequest);
        System.out.println("----------------------------------------------------");
        System.out.println(timeLogEntity);
        return timeLogRepository.save(timeLogEntity);
    }

    /**
     * Retrieves time logs based on various filters.
     *
     * @param employeeId    the ID of the employee (optional)
     * @param date          the date of the time log (optional)
     * @param clockInTime   the clock-in time (optional)
     * @param clockOutTime  the clock-out time (optional)
     * @param pageable      pagination information
     * @return a paginated list of TimeLogResponse
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
     * Retrieves a time log by its ID.
     *
     * @param id the ID of the time log
     * @return the TimeLogResponse containing time log details
     */
    public TimeLogResponse getTimeLogById(long id) {
        return timeLogMapper.toResponse(timeLogRepository.findById(id).get());
    }

    /*
    public List<TimeLogResponse> getAllTimeLogs() {
        return timeLogMapper.toResponseList(timeLogRepository.findAll());
    }


    public List<TimeLogResponse> getTimeLogByDate(String date) {
        LocalDate localDate = LocalDate.parse(date);
        return timeLogMapper.toResponseList(timeLogRepository.findByDate(localDate));
    }

    public List<TimeLogResponse> getTimeLogByClockInTime(String clockInTime) {
        LocalTime localTime = LocalTime.parse(clockInTime);
        return timeLogMapper.toResponseList(timeLogRepository.findByClockInTime(localTime));
    }

    public List<TimeLogResponse> getTimeLogByClockOutTime(String clockOutTime) {
        LocalTime localTime = LocalTime.parse(clockOutTime);
        return timeLogMapper.toResponseList(timeLogRepository.findByClockOutTime(localTime));
    }

     */

    /**
     * Updates an existing time log entry by its ID.
     * @param id the ID of the time log to update
     * @param timeLogRequest the request containing updated time log details
     * @return the updated TimeLogResponse
     */
    public TimeLogResponse updateTimeLog(long id, TimeLogRequest timeLogRequest) {
        TimeLogEntity timeLogEntity = timeLogMapper.toEntity(timeLogRequest);
        timeLogEntity.setId(id);
        return timeLogMapper.toResponse(timeLogRepository.save(timeLogEntity));
    }

}
