package org.stockify.controller.shift;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.shift.ShiftCreateRequest;
import org.stockify.dto.request.shift.ShiftFilterRequest;
import org.stockify.dto.response.shift.ShiftResponse;
import org.stockify.model.assembler.ShiftModelAssembler;
import org.stockify.model.service.ShiftService;

import java.net.URI;

@RestController
@RequestMapping("/shifts")
@Tag(name = "Shifts", description = "API REST for managing employee work shifts")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ShiftsController {

    private final ShiftService shiftService;
    private final ShiftModelAssembler shiftModelAssembler;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<EntityModel<ShiftResponse>> saveShift(@RequestBody @Valid ShiftCreateRequest request) {
        ShiftResponse savedShift = shiftService.save(request);
        return ResponseEntity
                .created(URI.create("/shifts/" + savedShift.getId()))
                .body(shiftModelAssembler.toModel(savedShift));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<PagedModel<EntityModel<ShiftResponse>>> listShifts(
            @ParameterObject ShiftFilterRequest filterRequest,
            @Parameter(hidden = true) @PageableDefault(sort = "day", direction = Sort.Direction.DESC) Pageable pageable,
            PagedResourcesAssembler<ShiftResponse> assembler) {
        Page<ShiftResponse> shifts = shiftService.findAll(filterRequest, pageable);
        return ResponseEntity.ok(assembler.toModel(shifts, shiftModelAssembler));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<EntityModel<ShiftResponse>> getShift(@PathVariable Long id) {
        return ResponseEntity.ok(shiftModelAssembler.toModel(shiftService.findById(id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        shiftService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN', 'MANAGER')")
    public ResponseEntity<PagedModel<EntityModel<ShiftResponse>>> listMyShifts(
            @Parameter(hidden = true) @PageableDefault(sort = "day", direction = Sort.Direction.DESC) Pageable pageable,
            PagedResourcesAssembler<ShiftResponse> assembler) {
        Page<ShiftResponse> shifts = shiftService.getMyShifts(pageable);
        return ResponseEntity.ok(assembler.toModel(shifts, shiftModelAssembler));
    }
}