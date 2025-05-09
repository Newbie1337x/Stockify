package org.stockify.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.model.dto.request.PosAmountRequest;
import org.stockify.model.dto.request.PosRequest;
import org.stockify.model.dto.response.PosResponse;
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

    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchAmount(@PathVariable long id, @RequestBody @Valid PosAmountRequest posAmountRequest)
    {
        posService.patchAmount(id,posAmountRequest);
        return ResponseEntity.ok().build();
    }

}
