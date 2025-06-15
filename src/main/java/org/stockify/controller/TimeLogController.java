package org.stockify.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
@Tag(name = "Time Logs", description = "API REST for managing employee time logs (clock-in and clock-out records)")
@RequiredArgsConstructor
public class TimeLogController {
    private final TimeLogService timeLogService;

    @Operation(
        summary = "Get all time logs with filters",
        description = "Get a paginated list of employee time logs with optional filters. " +
                      "Filters include ID, date, clock-in time, and clock-out time."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "List of time logs returned successfully",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(responseCode = "400", description = "Filter parameters are invalid")
    })
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<TimeLogResponse>>> getAllTimeLogs(
            @Parameter(description = "Time log ID to filter")
            @RequestParam(required = false) Long id,

            @Parameter(description = "Date to filter time logs (format: yyyy-MM-dd)")
            @RequestParam(required = false) String date,

            @Parameter(description = "Clock-in time to filter (format: HH:mm:ss)")
            @RequestParam(required = false) String clockInTime,

            @Parameter(description = "Clock-out time to filter (format: HH:mm:ss)")
            @RequestParam(required = false) String clockOutTime,

            Pageable pageable,
            PagedResourcesAssembler<TimeLogResponse> assembler
    ) {
        var pagedResult = timeLogService.getTimeLogs(id, date, clockInTime, clockOutTime, pageable);
        return ResponseEntity.ok(assembler.toModel(pagedResult));
    }
//Add hateoas

    @Operation(
        summary = "Create a new time log",
        description = "Create a new time log record for an employee's clock-in or clock-out event."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Time log created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TimeLogEntity.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid time log data provided"),
        @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @PostMapping
    public ResponseEntity<TimeLogEntity> addTimeLog(
            @Parameter(description = "Time log data to create", required = true)
            @Validated @RequestBody TimeLogRequest timeLogRequest) {
        return ResponseEntity.status(201).body(timeLogService.createTimeLog(timeLogRequest));
    }
//Add hateoas
    @Operation(
        summary = "Update a time log completely",
        description = "Replace all data for an existing time log with the provided data."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Time log updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TimeLogResponse.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid time log data provided"),
        @ApiResponse(responseCode = "404", description = "Time log not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TimeLogResponse> putTimeLog(
            @Parameter(description = "ID of the time log to update", required = true)
            @PathVariable Long id, 

            @Parameter(description = "New time log data", required = true)
            @Validated @RequestBody TimeLogRequest timeLogRequest) {
        return ResponseEntity.status(201).body(timeLogService.updateTimeLog(id, timeLogRequest));
    }
//Add hateoas

    @Operation(
        summary = "Update a time log partially",
        description = "Update specific fields of an existing time log while leaving others unchanged."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Time log partially updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TimeLogResponse.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid time log data provided"),
        @ApiResponse(responseCode = "404", description = "Time log not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<TimeLogResponse> patchTimeLog(
            @Parameter(description = "ID of the time log to partially update", required = true)
            @PathVariable Long id, 

            @Parameter(description = "Partial time log data to update", required = true)
            @Validated @RequestBody TimeLogRequest timeLogRequest) {
        return ResponseEntity.status(201).body(timeLogService.updateTimeLog(id, timeLogRequest));
    }
}
