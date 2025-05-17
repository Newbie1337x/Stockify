package org.stockify.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.employee.TimeLogRequest;
import org.stockify.dto.response.TimeLogResponse;
import org.stockify.model.entity.TimeLogEntity;
import org.stockify.model.mapper.TimeLogMapper;
import org.stockify.model.repository.TimeLogRepository;

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
        TimeLogEntity timeLogEntity = timeLogMapper.toEntity(timeLogRequest);
        return timeLogRepository.save(timeLogEntity);
    }

    public List<TimeLogResponse> getAllTimeLogs() {
        return timeLogMapper.toResponseList(timeLogRepository.findAll());
    }

    public TimeLogResponse getTimeLogById(long id) {
        return timeLogMapper.toResponse(timeLogRepository.findById(id).get());
    }

    public TimeLogResponse updateTimeLog(long id, TimeLogRequest timeLogRequest) {
        TimeLogEntity timeLogEntity = timeLogMapper.toEntity(timeLogRequest);
        timeLogEntity.setId(id);
        return timeLogMapper.toResponse(timeLogRepository.save(timeLogEntity));
    }

    public void deleteTimeLog(long id) {
        timeLogRepository.deleteById(id);
    }

    

}
