package org.stockify.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.pos.PosAmountRequest;
import org.stockify.dto.request.pos.PosCreateRequest;
import org.stockify.dto.request.sessionpos.SessionPosCloseRequest;
import org.stockify.dto.request.sessionpos.SessionPosRequest;
import org.stockify.dto.response.PosResponse;
import org.stockify.dto.response.SessionPosCreateResponse;
import org.stockify.dto.response.SessionPosResponse;
import org.stockify.model.enums.Status;
import org.stockify.model.service.PosService;

import java.util.List;

@RestController
@RequestMapping("/api/pos")
@Tag(name = "POS", description = "API REST para gestionar los POS (Point of Sale)")
public class PosController {

    private final PosService posService;

    @Autowired
    public PosController(PosService posService) {
        this.posService = posService;
    }

    @Operation(summary = "Crear un nuevo POS")
    @ApiResponses(value = {

    })
    @PostMapping
    public ResponseEntity<PosResponse> postPos(@Valid @RequestBody PosCreateRequest posCreateRequest) {
        PosResponse response = posService.save(posCreateRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener todos los POS")
    @GetMapping
    public ResponseEntity<List<PosResponse>> getPos() {
        return ResponseEntity.ok(posService.findAll());
    }

    @Operation(summary = "Obtener un POS por ID")
    @GetMapping("/{id}")
    public ResponseEntity<PosResponse> getById(
            @Parameter(description = "ID del POS") @PathVariable Long id) {
        return ResponseEntity.ok(posService.findById(id));
    }

    @Operation(summary = "Obtener POS por estado")
    @GetMapping("/status")
    public ResponseEntity<List<PosResponse>> getByStatus(
            @Parameter(description = "Estado del POS") @RequestParam Status status) {
        return ResponseEntity.ok(posService.findByStatus(status));
    }

    @Operation(summary = "Actualizar el monto de un POS")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchAmount(
            @Parameter(description = "ID del POS") @PathVariable Long id,
            @RequestBody @Valid PosAmountRequest posAmountRequest) {
        posService.patchAmount(id, posAmountRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Abrir un POS")
    @PostMapping("/open/{id}")
    public ResponseEntity<SessionPosCreateResponse> openPos(
            @Parameter(description = "ID del POS") @PathVariable Long id,
            @Valid @RequestBody SessionPosRequest sessionPosRequest) {
        return ResponseEntity.ok(posService.openPos(id, sessionPosRequest));
    }

    @Operation(summary = "Cerrar un POS")
    @PatchMapping("/close/{id}")
    public ResponseEntity<SessionPosResponse> closePos(
            @Parameter(description = "ID del POS") @PathVariable Long id,
            @Valid @RequestBody SessionPosCloseRequest sessionPosRequest) {
        return ResponseEntity.ok(posService.closePos(id, sessionPosRequest));
    }
}
