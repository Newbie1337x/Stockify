package org.stockify.model.service;

import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class TimeLogService {

    private final TimeLogRepository timeLogRepository;
    private final TimeLogMapper timeLogMapper;

    @Autowired
    public TimeLogService(TimeLogRepository timeLogRepository, TimeLogMapper timeLogMapper) {
        this.timeLogRepository = timeLogRepository;
        this.timeLogMapper = timeLogMapper;
    }

    public TimeLogEntity createTimeLog(TimeLogRequest timeLogRequest) {
        System.out.println("---------------------------------------------------");
        System.out.println(timeLogRequest);
        TimeLogEntity timeLogEntity = timeLogMapper.toEntity(timeLogRequest);
        System.out.println("----------------------------------------------------");
        System.out.println(timeLogEntity);
        return timeLogRepository.save(timeLogEntity);
    }

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

    public TimeLogResponse updateTimeLog(long id, TimeLogRequest timeLogRequest) {
        TimeLogEntity timeLogEntity = timeLogMapper.toEntity(timeLogRequest);
        timeLogEntity.setId(id);
        return timeLogMapper.toResponse(timeLogRepository.save(timeLogEntity));
    }

    

}
