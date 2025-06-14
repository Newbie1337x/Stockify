package org.stockify.controller;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class TimeLogController {
    private final TimeLogService timeLogService;

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
//Add hateoas

    @PostMapping
    public ResponseEntity<TimeLogEntity> addTimeLog(@Validated @RequestBody TimeLogRequest timeLogRequest) {
        return ResponseEntity.status(201).body(timeLogService.createTimeLog(timeLogRequest));
    }
//Add hateoas
    @PutMapping("/{id}")
    public ResponseEntity<TimeLogResponse> putTimeLog(@PathVariable Long id, @Validated @RequestBody TimeLogRequest timeLogRequest) {
        return ResponseEntity.status(201).body(timeLogService.updateTimeLog(id, timeLogRequest));
    }
//Add hateoas

    @PatchMapping("/{id}")
    public ResponseEntity<TimeLogResponse> patchTimeLog(@PathVariable Long id, @Validated @RequestBody TimeLogRequest timeLogRequest) {
        return ResponseEntity.status(201).body(timeLogService.updateTimeLog(id, timeLogRequest));
    }
}
