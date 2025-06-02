package org.stockify.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.response.SessionPosResponse;
import org.stockify.model.assembler.SessionPosModelAssembler;
import org.stockify.model.entity.SessionPosEntity;
import org.stockify.model.service.SessionPosService;

@RestController
@RequestMapping("/pos/sessions")
@Tag(name = "POS Sessions", description = "API REST para gestionar las sesiones de POS (Point of Sale)")
public class SessionPosController {

    private final SessionPosService sessionPosService;
    private final SessionPosModelAssembler sessionPosModelAssembler;

    @Autowired
    public SessionPosController(SessionPosService sessionPosService, 
                               SessionPosModelAssembler sessionPosModelAssembler) {
        this.sessionPosService = sessionPosService;
        this.sessionPosModelAssembler = sessionPosModelAssembler;
    }

    @Operation(summary = "Obtener todas las sesiones de POS")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<SessionPosResponse>>> getAllSessions(
            @PageableDefault(size = 10) Pageable pageable,
            PagedResourcesAssembler<SessionPosResponse> assembler) {
        Page<SessionPosResponse> sessionResponses = sessionPosService.findAll(pageable);
        return ResponseEntity.ok(assembler.toModel(sessionResponses, sessionPosModelAssembler));
    }

    @Operation(summary = "Obtener una sesión de POS por ID")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<SessionPosResponse>> getSessionById(
            @Parameter(description = "ID de la sesión de POS") @PathVariable Long id) {
        SessionPosEntity sessionEntity = sessionPosService.findById(id);
        SessionPosResponse sessionResponse = sessionPosService.entityToDto(sessionEntity);
        return ResponseEntity.ok(sessionPosModelAssembler.toModel(sessionResponse));
    }
}