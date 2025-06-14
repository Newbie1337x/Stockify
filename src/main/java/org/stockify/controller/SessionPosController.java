package org.stockify.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.response.SessionPosResponse;
import org.stockify.model.assembler.SessionPosModelAssembler;
import org.stockify.model.entity.SessionPosEntity;
import org.stockify.model.mapper.SessionPosMapper;
import org.stockify.model.service.SessionPosService;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/sessions")
@Tag(name = "POS Sessions", description = "API REST para gestionar las sesiones de POS (Point of Sale)")
@RequiredArgsConstructor
public class SessionPosController {

    private final SessionPosService sessionPosService;
    private final SessionPosModelAssembler sessionPosModelAssembler;
    private SessionPosMapper sessionPosMapper;


//    @Operation(summary = "Obtener todas las sesiones de POS")
//    @GetMapping
//    public ResponseEntity<PagedModel<EntityModel<SessionPosResponse>>> getAllSessions(

//            @PageableDefault(size = 10) Pageable pageable,
//            PagedResourcesAssembler<SessionPosResponse> assembler) {
//        Page<SessionPosResponse> sessionResponses = sessionPosService.findAll(pageable);
//        return ResponseEntity.ok(assembler.toModel(sessionResponses, sessionPosModelAssembler));
//    }

    @Operation(
        summary = "Get session by ID",
        description = "Get details of a specific POS session by its ID. " +
                      "Returns session information including employee, POS, opening and closing times, and amounts."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Session found and returned successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SessionPosResponse.class))
        ),
        @ApiResponse(responseCode = "404", description = "Session not found"),
        //@ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<SessionPosResponse>> getSessionById(
            @Parameter(description = "Session Id") @PathVariable Long id) {
        SessionPosEntity sessionEntity = sessionPosService.findById(id);
        SessionPosResponse sessionResponse = sessionPosMapper.toDto(sessionEntity);
        return ResponseEntity.ok(sessionPosModelAssembler.toModel(sessionResponse));
    }

    @Operation(
        summary = "Get all POS sessions with filters",
        description = "Get a paginated list of POS sessions with optional filters. " +
                      "Filters include employee ID, POS ID, opening and closing times, amounts, and session status."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "List of sessions returned successfully",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(responseCode = "400", description = "Filter parameters are invalid"),
        //@ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<SessionPosResponse>>> getAllSessions(
            @Parameter(description = "Employee ID to filter sessions")
            @RequestParam(required = false) Long employeeId,

            @Parameter(description = "POS ID to filter sessions")
            @RequestParam(required = false) Long posId,

            @Parameter(description = "Begin date to filter for opening date" + " (format: yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime openingTimeStart,

            @Parameter(description = "End date to filter for opening date" + "(formato: yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime openingTimeEnd,

            @Parameter(description = "Beginning date to filter for closing date (format: yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime closeTimeStart,

            @Parameter(description = "End date to filter for closing date (format: yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime closeTimeEnd,

            @Parameter(description = "Minimal opening amount")
            @RequestParam(required = false) BigDecimal openingAmountMin,

            @Parameter(description = "Maximal opening amount")
            @RequestParam(required = false) BigDecimal openingAmountMax,

            @Parameter(description = "Minimal closing amount")
            @RequestParam(required = false) BigDecimal closeAmountMin,

            @Parameter(description = "Maximal closing amount")
            @RequestParam(required = false) BigDecimal closeAmountMax,

            @Parameter(description = "Session state (true = open, false = closed)")
            @RequestParam(required = false) Boolean isOpen,
            @PageableDefault Pageable pageable,
            PagedResourcesAssembler<SessionPosResponse> assembler) {

        Page<SessionPosResponse> sessionResponses = sessionPosService.findAllWithFilters(
                employeeId, posId, 
                openingTimeStart, openingTimeEnd, 
                closeTimeStart, closeTimeEnd, 
                openingAmountMin, openingAmountMax, 
                closeAmountMin, closeAmountMax, 
                isOpen, 
                pageable);

        return ResponseEntity.ok(assembler.toModel(sessionResponses, sessionPosModelAssembler));
    }
}
