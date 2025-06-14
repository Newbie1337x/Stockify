package org.stockify.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.pos.PosAmountRequest;
import org.stockify.dto.request.sessionpos.SessionPosCloseRequest;
import org.stockify.dto.request.sessionpos.SessionPosRequest;
import org.stockify.dto.response.PosResponse;
import org.stockify.dto.response.SessionPosCreateResponse;
import org.stockify.dto.response.SessionPosResponse;
import org.stockify.model.assembler.PosModelAssembler;
import org.stockify.model.assembler.SessionPosCreateModelAssembler;
import org.stockify.model.assembler.SessionPosModelAssembler;
import org.stockify.model.enums.Status;
import org.stockify.model.service.PosService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/pos")
@RequiredArgsConstructor
@Tag(name = "POS", description = "API REST para gestionar los POS (Point of Sale)")
public class PosController {

    private final PosService posService;
    private final PosModelAssembler posModelAssembler;
    private final SessionPosModelAssembler sessionPosModelAssembler;
    private final SessionPosCreateModelAssembler sessionPosCreateModelAssembler;

    @Operation(summary = "Crear un nuevo POS")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "POS creado exitosamente")
    })
    @PostMapping("/{idStore}")
    public ResponseEntity<EntityModel<PosResponse>> postPos(
            @Parameter(description = "ID de la tienda") @PathVariable Long idStore) {
        PosResponse response = posService.save(idStore);
        return new ResponseEntity<>(posModelAssembler.toModel(response), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener todos los POS con filtros opcionales")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado paginado de POS")
    })
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<PosResponse>>> getPos(
            @Parameter(description = "ID del POS") @RequestParam(required = false) Long id,
            @Parameter(description = "ID de la tienda") @RequestParam(required = false) Long storeId,
            @Parameter(description = "Estado del POS") @RequestParam(required = false) Status status,
            @Parameter(description = "ID del empleado") @RequestParam(required = false) Long employeeId,
            @Parameter(description = "Monto mínimo actual") @RequestParam(required = false) BigDecimal currentAmountMin,
            @Parameter(description = "Monto máximo actual") @RequestParam(required = false) BigDecimal currentAmountMax,
            @Parameter(hidden = true) @PageableDefault() Pageable pageable,
            PagedResourcesAssembler<PosResponse> assembler) {
        Page<PosResponse> posResponses = posService.findAllWithFilters(
                id, storeId, status, employeeId, currentAmountMin, currentAmountMax, pageable);
        return ResponseEntity.ok(assembler.toModel(posResponses, posModelAssembler));
    }

    @Operation(summary = "Obtener un POS por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "POS encontrado"),
            @ApiResponse(responseCode = "404", description = "POS no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PosResponse>> getById(
            @Parameter(description = "ID del POS") @PathVariable Long id) {
        PosResponse posResponse = posService.findById(id);
        return ResponseEntity.ok(posModelAssembler.toModel(posResponse));
    }

    @Operation(summary = "Actualizar el monto de un POS")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Monto actualizado exitosamente")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchAmount(
            @Parameter(description = "ID del POS") @PathVariable Long id,
            @RequestBody @Valid PosAmountRequest posAmountRequest) {
        posService.patchAmount(id, posAmountRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Abrir un POS")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "POS abierto exitosamente")
    })
    @PostMapping("/open/{id}")
    public ResponseEntity<EntityModel<SessionPosCreateResponse>> openPos(
            @Parameter(description = "ID del POS") @PathVariable Long id,
            @Valid @RequestBody SessionPosRequest sessionPosRequest) {
        SessionPosCreateResponse response = posService.openPos(id, sessionPosRequest);
        return ResponseEntity.ok(sessionPosCreateModelAssembler.toModel(response));
    }

    @Operation(summary = "Cerrar un POS")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "POS cerrado exitosamente")
    })
    @PatchMapping("/close/{id}")
    public ResponseEntity<EntityModel<SessionPosResponse>> closePos(
            @Parameter(description = "ID del POS") @PathVariable Long id,
            @Valid @RequestBody SessionPosCloseRequest sessionPosRequest) {
        SessionPosResponse response = posService.closePos(id, sessionPosRequest);
        return ResponseEntity.ok(sessionPosModelAssembler.toModel(response));
    }
}
