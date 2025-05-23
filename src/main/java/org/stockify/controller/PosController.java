package org.stockify.controller;

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
import org.stockify.dto.response.SessionPosResponse;
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
    public ResponseEntity<PosResponse> postPos(@Valid@RequestBody PosCreateRequest posCreateRequest) {
        PosResponse response = posService.save(posCreateRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PosResponse>> getPos() {
        return ResponseEntity.ok(posService.findAll());
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
    public ResponseEntity<Void> patchAmount(@PathVariable Long id, @RequestBody @Valid PosAmountRequest posAmountRequest)
    {
        posService.patchAmount(id,posAmountRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/open/{id}")
    public ResponseEntity<SessionPosResponse> openPos(@PathVariable Long id,@Valid @RequestBody SessionPosRequest sessionPosRequest)
    {
       return ResponseEntity.ok(posService.openPos(id,sessionPosRequest));
    }

    @PatchMapping("/close/{id}")
    public ResponseEntity<SessionPosResponse> closePos(@PathVariable Long id, @Valid @RequestBody SessionPosCloseRequest sessionPosRequest) {
        return ResponseEntity.ok(posService.closePos(id,sessionPosRequest));
    }


//    @Deprecated
//    @PatchMapping("/{id}/toggle")
//        public ResponseEntity<String>patchToggleStatus(@PathVariable long id)
//    {
//        return ResponseEntity.ok("Sucessfull the pos is" + posService.toggleStatus(id));
//    }


}
