package org.stockify.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.model.dto.request.PosAmountRequest;
import org.stockify.model.dto.request.PosRequest;
import org.stockify.model.dto.request.PosStatusRequest;
import org.stockify.model.dto.response.PosResponse;
import org.stockify.model.enums.Status;
import org.stockify.model.service.PosService;

import java.util.List;

@RestController
@RequestMapping("/pos")
public class PosController {

    private final PosService posService;
    @Autowired
    public PosController(PosService posService) {
        this.posService = posService;
    }

    @PostMapping
    public ResponseEntity<PosResponse> postPos(@Valid@RequestBody PosRequest posRequest) {
        PosResponse response = posService.save(posRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PosResponse>> getPos() {
        return posService.findAll().isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(posService.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<PosResponse> getById(@PathVariable Long id)
    {
        return ResponseEntity.ok(posService.findById(id));
    }

    @GetMapping("/status")
    public ResponseEntity<List<PosResponse>> getByStatus(@RequestParam Status status)
    {
        return ResponseEntity.ok(posService.findByStatus(status));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchAmount(@PathVariable Long id, @RequestBody @Valid PosAmountRequest posAmountRequest)
    {
        posService.patchAmount(id,posAmountRequest);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle")
        public ResponseEntity<String>patchToggleStatus(@PathVariable long id)
    {
        return ResponseEntity.ok("Sucessfull the pos is " + posService.toggleStatus(id));
    }


}
