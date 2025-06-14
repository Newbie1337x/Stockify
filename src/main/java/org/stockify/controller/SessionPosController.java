package org.stockify.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
        summary = "Obtener una sesión de POS por ID",
        description = "Recupera la información detallada de una sesión de POS específica utilizando su identificador único"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Sesión encontrada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SessionPosResponse.class))
        ),
        @ApiResponse(responseCode = "404", description = "Sesión no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<SessionPosResponse>> getSessionById(
            @Parameter(description = "ID de la sesión de POS") @PathVariable Long id) {
        SessionPosEntity sessionEntity = sessionPosService.findById(id);
        SessionPosResponse sessionResponse = sessionPosMapper.toDto(sessionEntity);
        return ResponseEntity.ok(sessionPosModelAssembler.toModel(sessionResponse));
    }

    @Operation(
        summary = "Obtener todas las sesiones de POS",
        description = "Recupera una lista paginada de sesiones de POS con opciones de filtrado por múltiples criterios"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Lista de sesiones recuperada exitosamente",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(responseCode = "400", description = "Parámetros de filtrado inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<SessionPosResponse>>> getAllSessions(
            @Parameter(description = "ID del empleado")
            @RequestParam(required = false) Long employeeId,

            @Parameter(description = "ID del POS")
            @RequestParam(required = false) Long posId,

            @Parameter(description = "Fecha de inicio para filtrar por fecha de apertura (formato: yyyy-MM-dd'T'HH:mm:ss)") 
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime openingTimeStart,

            @Parameter(description = "Fecha de fin para filtrar por fecha de apertura (formato: yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime openingTimeEnd,

            @Parameter(description = "Fecha de inicio para filtrar por fecha de cierre (formato: yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime closeTimeStart,

            @Parameter(description = "Fecha de fin para filtrar por fecha de cierre (formato: yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime closeTimeEnd,

            @Parameter(description = "Monto mínimo de apertura")
            @RequestParam(required = false) BigDecimal openingAmountMin,

            @Parameter(description = "Monto máximo de apertura")
            @RequestParam(required = false) BigDecimal openingAmountMax,

            @Parameter(description = "Monto mínimo de cierre")
            @RequestParam(required = false) BigDecimal closeAmountMin,

            @Parameter(description = "Monto máximo de cierre")
            @RequestParam(required = false) BigDecimal closeAmountMax,

            @Parameter(description = "Estado de la sesión (true = abierta, false = cerrada)")
            @RequestParam(required = false) Boolean isOpen,
            @PageableDefault(size = 10) Pageable pageable,
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
