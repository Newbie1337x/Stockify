package org.stockify.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.employee.TimeLogRequest;
import org.stockify.dto.response.TimeLogResponse;
import org.stockify.model.entity.TimeLogEntity;
import org.stockify.model.mapper.TimeLogMapper;
import org.stockify.model.repository.TimeLogRepository;

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
    
    public List<TimeLogResponse> getTimeLogs(Long id, String date, String clockInTime, String clockOutTime) {
        List<TimeLogResponse> timeLogResponses = new ArrayList<>();
        if (id != null){
            timeLogResponses.add(getTimeLogById(id));
            return timeLogResponses;
        } else if (date != null) {
            timeLogResponses = getTimeLogByDate(date);
            return timeLogResponses;
        }
        else if (clockInTime != null) {
            timeLogResponses = getTimeLogByClockInTime(clockInTime);
            return timeLogResponses;
        }
        else if (clockOutTime != null) {
            timeLogResponses = getTimeLogByClockOutTime(clockOutTime);
            return timeLogResponses;
        }
        return getAllTimeLogs();
    }

    public List<TimeLogResponse> getAllTimeLogs() {
        return timeLogMapper.toResponseList(timeLogRepository.findAll());
    }

    public TimeLogResponse getTimeLogById(long id) {
        return timeLogMapper.toResponse(timeLogRepository.findById(id).get());
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

    public TimeLogResponse updateTimeLog(long id, TimeLogRequest timeLogRequest) {
        TimeLogEntity timeLogEntity = timeLogMapper.toEntity(timeLogRequest);
        timeLogEntity.setId(id);
        return timeLogMapper.toResponse(timeLogRepository.save(timeLogEntity));
    }

    public void deleteTimeLog(long id) {
        timeLogRepository.deleteById(id);
    }

    

}
