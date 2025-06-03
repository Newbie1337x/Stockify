package org.stockify.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.employee.TimeLogRequest;
import org.stockify.dto.response.TimeLogResponse;
import org.stockify.model.entity.TimeLogEntity;
import org.stockify.model.service.TimeLogService;

@RestController
@RequestMapping("/timelogs")
public class TimeLogController {
    private final TimeLogService timeLogService;

    @Autowired
    public TimeLogController(TimeLogService timeLogService) {
        this.timeLogService = timeLogService;
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<TimeLogResponse>>> getAllTimeLogs(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String clockInTime,
            @RequestParam(required = false) String clockOutTime,
            Pageable pageable,
            PagedResourcesAssembler<TimeLogResponse> assembler
    ) {
        var pagedResult = timeLogService.getTimeLogs(id, date, clockInTime, clockOutTime, pageable);
        return ResponseEntity.ok(assembler.toModel(pagedResult));
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
}
