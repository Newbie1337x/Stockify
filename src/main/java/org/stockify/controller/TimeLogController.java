package org.stockify.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.employee.TimeLogRequest;
import org.stockify.dto.response.TimeLogResponse;
import org.stockify.model.entity.TimeLogEntity;
import org.stockify.model.service.TimeLogService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/timelogs")
public class TimeLogController {
    private final TimeLogService timeLogService;

    @Autowired
    public TimeLogController(TimeLogService timeLogService) {
        this.timeLogService = timeLogService;
    }

    @GetMapping
    public ResponseEntity<List<TimeLogResponse>> getAllTimeLogs(
            @RequestParam (required = false) Long id,
            @RequestParam (required = false) String date,
            @RequestParam (required = false) String clockInTime,
            @RequestParam (required = false) String clockOutTime
            ) {
        List<TimeLogResponse> listTimeLog = timeLogService.getTimeLogs(id, date, clockInTime, clockOutTime);
        return ResponseEntity.ok(listTimeLog);
    }

    @PostMapping
    public ResponseEntity<TimeLogEntity> addTimeLog(@Validated @RequestBody TimeLogRequest timeLogRequest) {
        return ResponseEntity.status(201).body(timeLogService.createTimeLog(timeLogRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TimeLogResponse> putTimeLog(@PathVariable Long id, @Validated @RequestBody TimeLogRequest timeLogRequest) {
        return ResponseEntity.status(201).body(timeLogService.updateTimeLog(id, timeLogRequest));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TimeLogResponse> patchTimeLog(@PathVariable Long id, @Validated @RequestBody TimeLogRequest timeLogRequest) {
        return ResponseEntity.status(201).body(timeLogService.updateTimeLog(id, timeLogRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TimeLogResponse> deleteTimeLog(@PathVariable Long id) {
        timeLogService.deleteTimeLog(id);
        return ResponseEntity.status(204).build();
    }

}
