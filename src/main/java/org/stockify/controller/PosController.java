package org.stockify.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.stockify.dto.request.pos.PosCreateRequest;
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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pos")
@Tag(name = "POS", description = "API REST para gestionar los POS (Point of Sale)")
public class PosController {

    private final PosService posService;
    private final PosModelAssembler posModelAssembler;
    private final SessionPosModelAssembler sessionPosModelAssembler;
    private final SessionPosCreateModelAssembler sessionPosCreateModelAssembler;

    @Autowired
    public PosController(PosService posService, 
                         PosModelAssembler posModelAssembler,
                         SessionPosModelAssembler sessionPosModelAssembler,
                         SessionPosCreateModelAssembler sessionPosCreateModelAssembler) {
        this.posService = posService;
        this.posModelAssembler = posModelAssembler;
        this.sessionPosModelAssembler = sessionPosModelAssembler;
        this.sessionPosCreateModelAssembler = sessionPosCreateModelAssembler;
    }

    @Operation(summary = "Crear un nuevo POS")
    @ApiResponses(value = {

    })
    @PostMapping
    public ResponseEntity<EntityModel<PosResponse>> postPos(@Valid @RequestBody PosCreateRequest posCreateRequest) {
        PosResponse response = posService.save(posCreateRequest);
        return new ResponseEntity<>(posModelAssembler.toModel(response), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener todos los POS")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<PosResponse>>> getPos(
            @PageableDefault(size = 10) Pageable pageable,
            PagedResourcesAssembler<PosResponse> assembler) {
        Page<PosResponse> posResponses = posService.findAll(pageable);
        return ResponseEntity.ok(assembler.toModel(posResponses, posModelAssembler));
    }

    @Operation(summary = "Obtener un POS por ID")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PosResponse>> getById(
            @Parameter(description = "ID del POS") @PathVariable Long id) {
        PosResponse posResponse = posService.findById(id);
        return ResponseEntity.ok(posModelAssembler.toModel(posResponse));
    }

    @Operation(summary = "Obtener POS por estado")
    @GetMapping("/status")
    public ResponseEntity<PagedModel<EntityModel<PosResponse>>> getByStatus(
            @Parameter(description = "Estado del POS") @RequestParam Status status,
            @PageableDefault(size = 10) Pageable pageable,
            PagedResourcesAssembler<PosResponse> assembler) {
        Page<PosResponse> posResponses = posService.findByStatus(status, pageable);
        return ResponseEntity.ok(assembler.toModel(posResponses, posModelAssembler));
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
    public ResponseEntity<EntityModel<SessionPosCreateResponse>> openPos(
            @Parameter(description = "ID del POS") @PathVariable Long id,
            @Valid @RequestBody SessionPosRequest sessionPosRequest) {
        SessionPosCreateResponse response = posService.openPos(id, sessionPosRequest);
        return ResponseEntity.ok(sessionPosCreateModelAssembler.toModel(response));
    }

    @Operation(summary = "Cerrar un POS")
    @PatchMapping("/close/{id}")
    public ResponseEntity<EntityModel<SessionPosResponse>> closePos(
            @Parameter(description = "ID del POS") @PathVariable Long id,
            @Valid @RequestBody SessionPosCloseRequest sessionPosRequest) {
        SessionPosResponse response = posService.closePos(id, sessionPosRequest);
        return ResponseEntity.ok(sessionPosModelAssembler.toModel(response));
    }
}
