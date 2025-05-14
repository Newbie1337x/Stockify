package org.stockify.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.dto.request.ProviderRequestDTO;
import org.stockify.dto.response.ProviderResponseDTO;
import org.stockify.model.entities.ProviderEntity;
import org.stockify.model.mapper.ProviderMapper;
import org.stockify.model.services.ProviderService;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/provider")
public class ProviderController {

    private final ProviderService providerService;
    private final ProviderMapper providerMapper;
    @Autowired
    public ProviderController(ProviderService providerService, ProviderMapper providerMapper) {
        this.providerService = providerService;
        this.providerMapper = providerMapper;
    }

    //---Crud operations---

    /*
    @GetMapping("/active")
    public ResponseEntity<List<ProviderResponseDTO>> getActiveProviders() {
        List<ProviderEntity> provedores = providerService.findALlActive();
        if(provedores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<ProviderResponseDTO> dtosLista = providerMapper.toResponseDTOList(provedores);
        return ResponseEntity.ok(dtosLista);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProviderResponseDTO>> getProviders() {
        List<ProviderEntity> proveedores = providerService.findAll();
        if (proveedores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<ProviderResponseDTO> dtoList = providerMapper.toResponseDTOList(proveedores);
        return ResponseEntity.ok(dtoList);
    }
     */

    @GetMapping("/paginated")
    public ResponseEntity<Map<String, Object>> getActivePaginatedProviders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<ProviderEntity> providerPage = providerService.findAllPaginatedActive(page, size);
        if (providerPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<ProviderResponseDTO> providers = providerMapper.toResponseDTOList(providerPage.getContent());
        Map<String, Object> response = new HashMap<>();
        response.put("providers", providers);
        response.put("currentPage", providerPage.getNumber());
        response.put("totalItems", providerPage.getTotalElements());
        response.put("totalPages", providerPage.getTotalPages());
        response.put("hasNext", providerPage.hasNext());
        response.put("hasPrevious", providerPage.hasPrevious());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/allpaginated")
    public ResponseEntity<Map<String, Object>> getPaginatedProviders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<ProviderEntity> providerPage = providerService.findAllPaginated(page, size);
        if (providerPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<ProviderResponseDTO> providers = providerMapper.toResponseDTOList(providerPage.getContent());
        Map<String, Object> response = new HashMap<>();
        response.put("providers", providers);
        response.put("currentPage", providerPage.getNumber());
        response.put("totalItems", providerPage.getTotalElements());
        response.put("totalPages", providerPage.getTotalPages());
        response.put("hasNext", providerPage.hasNext());
        response.put("hasPrevious", providerPage.hasPrevious());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ProviderResponseDTO> getProviderById(@PathVariable Long id) {
        ProviderEntity proveedor = providerService.findById(id);
        return proveedor != null
                ? ResponseEntity.ok(providerMapper.toResponseDTO(proveedor))
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/name")
    public ResponseEntity<List<ProviderResponseDTO>> getProviderByNombre(@RequestParam String nombre) {
        List<ProviderEntity> proveedor = providerService.findByName(nombre);
        return proveedor != null
                ? ResponseEntity.ok(providerMapper.toResponseDTOList(proveedor))
                : ResponseEntity.noContent().build();
    }

    @GetMapping("/razon_social")
    public ResponseEntity<ProviderResponseDTO> getProviderByRazonSocial(@RequestParam String razonSocial) {
        ProviderEntity proveedor = providerService.findByRazonSocial(razonSocial);
        return proveedor != null
                ? ResponseEntity.ok(providerMapper.toResponseDTO(proveedor))
                : ResponseEntity.noContent().build();
    }

    @GetMapping("/cuit")
    public ResponseEntity<ProviderResponseDTO> getProviderByCuit(@RequestParam String cuit) {
        ProviderEntity proveedor = providerService.findByCuit(cuit);
        return proveedor != null
                ? ResponseEntity.ok(providerMapper.toResponseDTO(proveedor))
                : ResponseEntity.noContent().build();
    }

    @PostMapping
        public ResponseEntity<String> createProvider(@Valid @RequestBody ProviderRequestDTO requestDTO) {
            if (requestDTO == null) {
                return ResponseEntity.badRequest().body("El cuerpo de la solicitud no puede estar vacío");
            }
            ProviderEntity entity = providerMapper.toEntity(requestDTO);
            boolean created = providerService.save(entity);
            return created
                    ? ResponseEntity.ok("Proveedor creado correctamente")
                    : ResponseEntity.badRequest().body("Error al crear el proveedor o el proveedor ya existe");
        }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProvider(
            @PathVariable Long id,
            @Valid @RequestBody ProviderRequestDTO requestDTO) {
        ProviderEntity existing = providerService.findById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        ProviderEntity updated = providerMapper.toEntity(requestDTO);
        updated.setId(id);
        providerService.update(updated);

        return ResponseEntity.ok("Proveedor actualizado correctamente");
    }

    @PatchMapping("/delete/{id}")
    public ResponseEntity<String> logicalDeleteProvider(@PathVariable Long id) {
        ProviderEntity proveedor = providerService.findById(id);
        if (proveedor == null) {
            return ResponseEntity.notFound().build();
        }
        providerService.logicalDelete(proveedor);
        return ResponseEntity.ok("Proveedor dado de baja correctamente");
    }

    @DeleteMapping("/delete/permanent/{id}")
    public ResponseEntity<String> deleteProvider(@PathVariable Long id) {
        providerService.delete(providerService.findById(id));
        return ResponseEntity.ok("Proveedor eliminado correctamente");
    }
}
