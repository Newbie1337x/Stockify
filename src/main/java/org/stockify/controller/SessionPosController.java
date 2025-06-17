package org.stockify.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.sessionpos.SessionPosFiltersRequest;
import org.stockify.dto.response.SessionPosResponse;
import org.stockify.model.assembler.SessionPosModelAssembler;
import org.stockify.model.mapper.SessionPosMapper;
import org.stockify.model.service.SessionPosService;

@RestController
@RequestMapping("/sessions")
@Tag(name = "POS Sessions", description = "API REST para gestionar las sesiones de POS (Point of Sale)")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class SessionPosController {

    private final SessionPosService sessionPosService;
    private final SessionPosModelAssembler sessionPosModelAssembler;
    private final SessionPosMapper sessionPosMapper;


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
        @ApiResponse(responseCode = "404", description = "Session not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('READ') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('READ')")
    public ResponseEntity<EntityModel<SessionPosResponse>> getSessionById(
            @Parameter(description = "Session Id") @PathVariable Long id) {
        SessionPosResponse sessionResponse = sessionPosService.findById(id);
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
        @ApiResponse(responseCode = "400", description = "Filter parameters are invalid")
    })
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('READ') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('READ')")
    public ResponseEntity<PagedModel<EntityModel<SessionPosResponse>>> getAllSessions(
            @ParameterObject SessionPosFiltersRequest filters,
            @PageableDefault Pageable pageable,
            PagedResourcesAssembler<SessionPosResponse> assembler) {

        Page<SessionPosResponse> sessionResponses = sessionPosService.findAllWithFilters(filters, pageable);

        return ResponseEntity.ok(assembler.toModel(sessionResponses, sessionPosModelAssembler));
    }
}
