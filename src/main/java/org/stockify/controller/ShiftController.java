package org.stockify.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.shift.ShiftRequest;
import org.stockify.dto.response.shift.ShiftResponse;
import org.stockify.hateoas.ShiftModelAssembler;
import org.stockify.model.service.ShiftService;
import java.net.URI;

@RestController
@RequestMapping("/shift")
public class ShiftController {
    private final ShiftService shiftService;
    private final ShiftModelAssembler shiftModelAssembler;

    public ShiftController(ShiftService shiftService, ShiftModelAssembler shiftModelAssembler) {
        this.shiftService = shiftService;
        this.shiftModelAssembler = shiftModelAssembler;
    }

    @PostMapping
    public ResponseEntity<EntityModel<ShiftResponse>> createShift(@Valid @RequestBody ShiftRequest shift) {
        ShiftResponse shiftResponse = shiftService.save(shift);
        EntityModel<ShiftResponse> entityModel = shiftModelAssembler.toModel(shiftResponse);
        return ResponseEntity
                .created(URI.create(entityModel.getRequiredLink("self").getHref()))
                .body(entityModel);
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<ShiftResponse>>> getAllShifts(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size,
            PagedResourcesAssembler<ShiftResponse> assembler) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Page<ShiftResponse> shiftResponsePage = shiftService.findAll(pageable);

        return ResponseEntity.ok(assembler.toModel(shiftResponsePage, shiftModelAssembler));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ShiftResponse>> getShiftById(@PathVariable Long id) {
        ShiftResponse shiftResponse = shiftService.findById(id);

        return ResponseEntity.ok(shiftModelAssembler.toModel(shiftResponse));
    }

    /// PUEDE IR '?' o 'Void' ¡?¡??¡
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShiftById(@PathVariable Long id) {
        shiftService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ShiftResponse>> putShift(@PathVariable Long id, @Valid @RequestBody ShiftRequest shiftRequest) {
        ShiftResponse updatedShift = shiftService.updateShiftFull(id, shiftRequest);
        EntityModel<ShiftResponse> entityModel = shiftModelAssembler.toModel(updatedShift);

        return ResponseEntity.ok(entityModel);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<ShiftResponse>> patchShift(@PathVariable Long id, @Valid @RequestBody ShiftRequest shiftRequest) {
        ShiftResponse updatedShift = shiftService.updateShiftPartial(id, shiftRequest);
        EntityModel<ShiftResponse> entityModel = shiftModelAssembler.toModel(updatedShift);

        return ResponseEntity.ok(entityModel);
    }
}
