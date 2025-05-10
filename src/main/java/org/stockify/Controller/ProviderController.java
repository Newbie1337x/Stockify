package org.stockify.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stockify.Model.Entities.ProviderEntity;
import org.stockify.Model.Services.ProviderService;

import java.util.Collections;
import java.util.List;

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
    public String createProvider(ProviderEntity providerEntity) {
        providerService.save(providerEntity);
        return "Provider created";
    }

    @PutMapping
    public String updateProvider() {
        return "Provider updated";
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
