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
            @RequestParam(required = false) String mail,
            @RequestParam(required = false) String CUIT,
            @RequestParam(required = false) String direFiscal,
            @RequestParam(required = false) String razonSocial){
         {
            if (id == null && nombre == null && mail == null && CUIT == null && direFiscal == null && razonSocial == null) {
                List<ProviderEntity> proveedores = providerService.findAll();
                return proveedores.isEmpty()
                        ? ResponseEntity.noContent().build()
                        : ResponseEntity.ok(proveedores);
            }
            if (id != null) {
                ProviderEntity proveedor = providerService.findById(id);
                return proveedor != null
                        ? ResponseEntity.ok(Collections.singletonList(proveedor))
                        : ResponseEntity.notFound().build();
            }
            if (nombre != null) {
                ProviderEntity proveedor = providerService.findByName(nombre);
                return proveedor != null
                        ? ResponseEntity.ok(Collections.singletonList(proveedor))
                        : ResponseEntity.noContent().build();
            }
            if (mail != null) {
                ProviderEntity proveedor = providerService.findByEmail(mail);
                return proveedor != null
                        ? ResponseEntity.ok(Collections.singletonList(proveedor))
                        : ResponseEntity.noContent().build();
            }
            if (CUIT !=null){
                ProviderEntity proveedor = providerService.findByCUIT(CUIT);
                return proveedor != null
                        ? ResponseEntity.ok(Collections.singletonList(proveedor))
                        : ResponseEntity.noContent().build();
            }
             if (direFiscal !=null){
                 ProviderEntity proveedor = providerService.findByDireccionFiscal(direFiscal);
                 return proveedor != null
                         ? ResponseEntity.ok(Collections.singletonList(proveedor))
                         : ResponseEntity.noContent().build();
             }
             if (razonSocial !=null){
                 ProviderEntity proveedor = providerService.findByRazonSocial(razonSocial);
                 return proveedor != null
                         ? ResponseEntity.ok(Collections.singletonList(proveedor))
                         : ResponseEntity.noContent().build();
             }
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> createProvider(@RequestBody ProviderEntity providerEntity) {
        if(providerService.save(providerEntity)) {
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
        } //comprobacion extra por si coloca un id que no buscaba reemplazar
        providerService.update(providerEntity);
        return ResponseEntity.ok("Proveedor actualizado correctamente");
    }

    @PatchMapping("/delete")
    public ResponseEntity<String> logicalDeleteProvider(@PathVariable Long id) {
        providerService.logicalDelete(providerService.findById(id));
        return ResponseEntity.ok("Proveedor dado de baja correctamente");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteProvider(@PathVariable Long id) {
        providerService.delete(providerService.findById(id));
        return ResponseEntity.ok("Proveedor eliminado correctamente");
    }
}
