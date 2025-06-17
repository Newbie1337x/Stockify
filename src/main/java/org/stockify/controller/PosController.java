package org.stockify.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
import org.stockify.dto.request.pos.PosFilterRequest;
import org.stockify.dto.request.sessionpos.SessionPosCloseRequest;
import org.stockify.dto.request.sessionpos.SessionPosRequest;
import org.stockify.dto.response.PosResponse;
import org.stockify.dto.response.SessionPosCreateResponse;
import org.stockify.dto.response.SessionPosResponse;
import org.stockify.model.assembler.PosModelAssembler;
import org.stockify.model.assembler.SessionPosCreateModelAssembler;
import org.stockify.model.assembler.SessionPosModelAssembler;
import org.stockify.model.service.PosService;


@RestController
@RequestMapping("/pos")
@RequiredArgsConstructor
@Tag(name = "POS", description = "API REST for POS management (Point of Sale)")
@SecurityRequirement(name = "bearerAuth")
public class PosController {

    private final PosService posService;
    private final PosModelAssembler posModelAssembler;
    private final SessionPosModelAssembler sessionPosModelAssembler;
    private final SessionPosCreateModelAssembler sessionPosCreateModelAssembler;

    @Operation(summary = "Create a new POS")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "POS created successfully"),
    })
    @PostMapping("/{idStore}")
    public ResponseEntity<EntityModel<PosResponse>> postPos(
            @Parameter(description = "Store ID") @PathVariable Long idStore) {
        PosResponse response = posService.save(idStore);
        return new ResponseEntity<>(posModelAssembler.toModel(response), HttpStatus.CREATED);
    }

    @Operation(summary = "Get a paginated list of POS with optional filters")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paged list of POS returned successfully"),
    })
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<PosResponse>>> getPos(PosFilterRequest spec,
                                                                       @Parameter(hidden = true) @PageableDefault() Pageable pageable,
                                                                       PagedResourcesAssembler<PosResponse> assembler) {
        Page<PosResponse> posResponses = posService.findAllWithFilters(spec, pageable);
        return ResponseEntity.ok(assembler.toModel(posResponses, posModelAssembler));
    }

    @Operation(summary = "Get a POS by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "POS found"),
            @ApiResponse(responseCode = "404", description = "POS not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PosResponse>> getById(
            @Parameter(description = "POS ID") @PathVariable Long id) {
        PosResponse posResponse = posService.findById(id);
        return ResponseEntity.ok(posModelAssembler.toModel(posResponse));
    }

    @Operation(summary = "Amount update for a POS")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Amount updated successfully"),
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchAmount(
            @Parameter(description = "POS ID") @PathVariable Long id,
            @RequestBody @Valid PosAmountRequest posAmountRequest) {
        posService.addAmount(id, posAmountRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "POS open operation")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "POS open successfully"),
    })
    @PostMapping("/open/{id}")
    public ResponseEntity<EntityModel<SessionPosCreateResponse>> openPos(
            @Parameter(description = "ID del POS") @PathVariable Long id,
            @Valid @RequestBody SessionPosRequest sessionPosRequest) {
        SessionPosCreateResponse response = posService.openPos(id, sessionPosRequest);
        return ResponseEntity.ok(sessionPosCreateModelAssembler.toModel(response));
    }

    @Operation(summary = "POS close operation", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "POS closed successfully"),
    })
    @PatchMapping("/close/{id}")
    public ResponseEntity<EntityModel<SessionPosResponse>> closePos(
            @Parameter(description = "POS ID") @PathVariable Long id,
            @Valid @RequestBody SessionPosCloseRequest sessionPosRequest) {

        SessionPosResponse response = posService.closePos(id, sessionPosRequest);
        return ResponseEntity.ok(sessionPosModelAssembler.toModel(response));
    }
}
