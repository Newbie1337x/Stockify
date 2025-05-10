package org.stockify.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.Model.Entities.ProviderEntity;
import org.stockify.Model.Services.ProviderService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/provider")
public class ProviderController {

    @Autowired
    private ProviderService providerService;

    //---Crud operations---

    @GetMapping
    public ResponseEntity<List<ProviderEntity>> getProvider(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String mail) {
         {

            if (id == null && nombre == null && mail == null) {
                List<ProviderEntity> usuarios = providerService.findAll();
                return usuarios.isEmpty()
                        ? ResponseEntity.noContent().build()
                        : ResponseEntity.ok(usuarios);
            }
            if (id != null) {
                ProviderEntity usuario = providerService.findById(id);
                return usuario != null
                        ? ResponseEntity.ok(Collections.singletonList(usuario))
                        : ResponseEntity.notFound().build();
            }
            if (nombre != null) {
                ProviderEntity usuarios = providerService.findByName(nombre);
                return usuarios != null
                        ? ResponseEntity.ok(Collections.singletonList(usuarios))
                        : ResponseEntity.noContent().build();
            }
            if (mail != null) {
                ProviderEntity usuario = providerService.findByEmail(mail);
                return usuario != null
                        ? ResponseEntity.ok(Collections.singletonList(usuario))
                        : ResponseEntity.noContent().build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> createProvider(@RequestBody ProviderEntity providerEntity) {
        if(providerService.save(providerEntity)) {;
            return ResponseEntity.ok("proveedor creado correctamente");
        }
        return ResponseEntity.badRequest().body("Error al crear el proveedor");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProvider(
            @PathVariable Long id,
            @RequestBody ProviderEntity providerEntity) {
        if (providerEntity.getId() == null || !id.equals(providerEntity.getId())) {
            return ResponseEntity.badRequest().body("El ID del path y del cuerpo deben coincidir y no pueden ser nulos");
        }
        providerService.update(providerEntity);
        return ResponseEntity.ok("Proveedor actualizado correctamente");
    }

    @PatchMapping
    public String patchProvider() {
        return "Provider patched";
    }

    @DeleteMapping
    public String deleteProvider() {
        return "Provider deleted";
    }
}
