package org.stockify.controller.shift;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.shift.ShiftFilterRequest;
import org.stockify.dto.request.shift.ShiftRequest;
import org.stockify.dto.response.shift.ShiftResponse;
import org.stockify.model.assembler.ShiftModelAssembler;
import org.stockify.model.service.ShiftService;
import java.net.URI;

@RestController
@RequestMapping("/shifts")
@Tag(name = "Shifts", description = "API REST for managing employee work shifts")
@RequiredArgsConstructor
public class ShiftController {
    private final ShiftService shiftService;
    private final ShiftModelAssembler shiftModelAssembler;


    @Operation(
        summary = "Create a new shift",
        description = "Create a new work shift with the provided details. Returns the created shift with its ID and HATEOAS links."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Shift created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ShiftResponse.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid shift data provided"),
        @ApiResponse(responseCode = "404", description = "Referenced entity not found")
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_MANAGER') and hasAuthority('WRITE') or " +
            "hasRole('ROLE_ADMIN') and hasAuthority('WRITE')")
    public ResponseEntity<EntityModel<ShiftResponse>> createShift(
            @Parameter(description = "Shift data to create", required = true)
            @Valid @RequestBody ShiftRequest shiftRequest) {
        ShiftResponse shiftResponse = shiftService.save(shiftRequest);
        EntityModel<ShiftResponse> entityModel = shiftModelAssembler.toModel(shiftResponse);
        return ResponseEntity
                .created(URI.create(entityModel.getRequiredLink("self").getHref()))
                .body(entityModel);
    }

    @Operation(
        summary = "Get all shifts with filters",
        description = "Get a paginated list of work shifts with optional filters. " +
                      "Results are sorted by day in descending order."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "List of shifts returned successfully",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(responseCode = "400", description = "Filter parameters are invalid")
    })
    @GetMapping
    @PreAuthorize("hasRole('ROLE_MANAGER') and hasAuthority('READ') or " +
            "hasRole('ROLE_ADMIN') and hasAuthority('READ')")
    public ResponseEntity<PagedModel<EntityModel<ShiftResponse>>> getAllShifts(
            @Parameter(description = "Filter criteria for shifts")
            @ModelAttribute ShiftFilterRequest filterRequest,

            @Parameter(description = "Page number (zero-based)")
            @RequestParam(required = false, defaultValue = "0") int page,

            @Parameter(description = "Number of items per page")
            @RequestParam(required = false, defaultValue = "20") int size,

            PagedResourcesAssembler<ShiftResponse> assembler) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("day").descending());
        Page<ShiftResponse> shiftResponsePage = shiftService.findAll(filterRequest, pageable);

        return ResponseEntity.ok(assembler.toModel(shiftResponsePage, shiftModelAssembler));
    }

    @Operation(
        summary = "Get shift by ID",
        description = "Get details of a specific work shift by its ID. Returns shift information with HATEOAS links."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Shift found and returned successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ShiftResponse.class))
        ),
        @ApiResponse(responseCode = "404", description = "Shift not found")
    })
    @GetMapping("/{shiftID}")
    @PreAuthorize("hasRole('ROLE_MANAGER') and hasAuthority('READ') or " +
            "hasRole('ROLE_ADMIN') and hasAuthority('READ')")
    public ResponseEntity<EntityModel<ShiftResponse>> getShiftById(
            @Parameter(description = "ID of the shift to retrieve", required = true)
            @PathVariable Long shiftID) {
        ShiftResponse shiftResponse = shiftService.findById(shiftID);

        return ResponseEntity.ok(shiftModelAssembler.toModel(shiftResponse));
    }

    @Operation(
        summary = "Delete a shift",
        description = "Delete a work shift by its ID. Returns no content on successful deletion."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204", 
            description = "Shift deleted successfully"
        ),
        @ApiResponse(responseCode = "404", description = "Shift not found"),
        @ApiResponse(responseCode = "409", description = "Shift cannot be deleted due to dependencies")
    })
    @DeleteMapping("/{shiftID}")
    @PreAuthorize("hasRole('ROLE_MANAGER') and hasAuthority('DELETE') or " +
            "hasRole('ROLE_ADMIN') and hasAuthority('DELETE')")
    public ResponseEntity<Void> deleteShiftById(
            @Parameter(description = "ID of the shift to delete", required = true)
            @PathVariable Long shiftID) {
        shiftService.delete(shiftID);

        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Update a shift completely",
        description = "Replace all data for an existing shift with the provided data. Returns the updated shift with HATEOAS links."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Shift updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ShiftResponse.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid shift data provided"),
        @ApiResponse(responseCode = "404", description = "Shift not found")
    })
    @PutMapping("/{shiftID}")
    @PreAuthorize("hasRole('ROLE_MANAGER') and hasAuthority('WRITE') or " +
            "hasRole('ROLE_ADMIN') and hasAuthority('WRITE')")
    public ResponseEntity<EntityModel<ShiftResponse>> putShift(
            @Parameter(description = "ID of the shift to update", required = true)
            @PathVariable Long shiftID, 

            @Parameter(description = "New shift data", required = true)
            @Valid @RequestBody ShiftRequest shiftRequest) {
        ShiftResponse updatedShift = shiftService.updateShiftFull(shiftID, shiftRequest);
        EntityModel<ShiftResponse> entityModel = shiftModelAssembler.toModel(updatedShift);

        return ResponseEntity.ok(entityModel);
    }

    @Operation(
        summary = "Update a shift partially",
        description = "Update specific fields of an existing shift while leaving others unchanged. Returns the updated shift with HATEOAS links."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Shift partially updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ShiftResponse.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid shift data provided"),
        @ApiResponse(responseCode = "404", description = "Shift not found")
    })
    @PatchMapping("/{shiftID}")
    @PreAuthorize("hasRole('ROLE_MANAGER') and hasAuthority('WRITE') or " +
            "hasRole('ROLE_ADMIN') and hasAuthority('WRITE')")
    public ResponseEntity<EntityModel<ShiftResponse>> patchShift(
            @Parameter(description = "ID of the shift to partially update", required = true)
            @PathVariable Long shiftID, 

            @Parameter(description = "Partial shift data to update", required = true)
            @Valid @RequestBody ShiftRequest shiftRequest) {
        ShiftResponse updatedShift = shiftService.updateShiftPartial(shiftID, shiftRequest);
        EntityModel<ShiftResponse> entityModel = shiftModelAssembler.toModel(updatedShift);

        return ResponseEntity.ok(entityModel);
    }
}
