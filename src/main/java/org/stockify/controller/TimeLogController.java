package org.stockify.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Time Logs", description = "Operations related to employee time tracking logs")
public class TimeLogController {
    private final TimeLogService timeLogService;

    @Autowired
    public TimeLogController(TimeLogService timeLogService) {
        this.timeLogService = timeLogService;
    }

    @Operation(
            summary = "Get time logs",
            description = "Retrieve a paginated list of time logs. Supports filtering by ID, date, clock-in time, and clock-out time.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Time logs retrieved successfully")
            }
    )
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

    @Operation(
            summary = "Create a new time log",
            description = "Creates a new time log entry for an employee.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Time log created successfully",
                            content = @Content(schema = @Schema(implementation = TimeLogEntity.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request",
                            content = @Content
                    )
            }
    )
    @PostMapping
    public ResponseEntity<TimeLogEntity> addTimeLog(@Validated @RequestBody TimeLogRequest timeLogRequest) {
        return ResponseEntity.status(201).body(timeLogService.createTimeLog(timeLogRequest));
    }

    @Operation(
            summary = "Replace a time log",
            description = "Fully updates an existing time log by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Time log updated successfully",
                            content = @Content(schema = @Schema(implementation = TimeLogResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Time log not found",
                            content = @Content
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<TimeLogResponse> putTimeLog(@PathVariable Long id, @Validated @RequestBody TimeLogRequest timeLogRequest) {
        return ResponseEntity.status(201).body(timeLogService.updateTimeLog(id, timeLogRequest));
    }

    @Operation(
            summary = "Partially update a time log",
            description = "Updates one or more fields of an existing time log by its ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Time log partially updated successfully",
                            content = @Content(schema = @Schema(implementation = TimeLogResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Time log not found",
                            content = @Content
                    )
            }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<TimeLogResponse> patchTimeLog(@PathVariable Long id, @Validated @RequestBody TimeLogRequest timeLogRequest) {
        return ResponseEntity.status(201).body(timeLogService.updateTimeLog(id, timeLogRequest));
    }
}
