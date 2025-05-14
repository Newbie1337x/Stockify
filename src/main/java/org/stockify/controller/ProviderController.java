package org.stockify.controller;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.ProviderRequest;
import org.stockify.dto.response.ProviderResponse;
import org.stockify.model.service.ProviderService;


@RestController
@RequestMapping("/provider")
public class ProviderController {

    private final ProviderService providerService;
    public ProviderController(ProviderService providerService) {
        this.providerService = providerService;
    }

    //---Crud operations---
    @GetMapping
    public ResponseEntity<Page<ProviderResponse>> listProviders(
            @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ResponseEntity.ok().body(providerService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProviderResponse> getProviderById(@PathVariable Long id) {
        return ResponseEntity.ok(providerService.findById(id));
    }

    @PostMapping
        public ResponseEntity<ProviderResponse> createProvider(@Valid @RequestBody ProviderRequest request) {
        return ResponseEntity.ok(providerService.save(request));
    }

    /*
    @PutMapping("/{id}")
    public ResponseEntity<ProviderResponse> updateProvider(
            @PathVariable Long id,
            @Valid @RequestBody ProviderRequest request) {
        return ResponseEntity.ok(providerService.update(request));
        }
    */

    @PatchMapping("/{id}/disable")
    public ResponseEntity<String> logicalDeleteProvider(@PathVariable Long id) {
        providerService.logicalDelete(id);
        return ResponseEntity.ok("Proveedor dado de baja correctamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProvider(@PathVariable Long id) {
        providerService.delete(id);
        return ResponseEntity.ok("Proveedor eliminado correctamente");
    }


    //FILTERS //CONSULTAR

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String businessName,
            @RequestParam(required = false) String taxId,
            Pageable pageable) {

        if (name != null) {
            return ResponseEntity.ok(providerService.findByName(pageable, name));
        } else if (businessName != null) {
            return ResponseEntity.ok(providerService.findByBusinessName(businessName));
        } else if (taxId != null) {
            return ResponseEntity.ok(providerService.findByTaxId(taxId));
        } else {
            return listProviders(pageable);
        }
    }
}
