package org.stockify.model.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.stockify.model.entities.ProviderEntity;
import org.stockify.model.exceptions.ProviderNotFoundException;
import org.stockify.model.repositories.ProviderRepo;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProviderService {

    private final ProviderRepo providerRepo;

    public ProviderService(ProviderRepo providerRepo){
        this.providerRepo = providerRepo;
    }

    //---Crud operations---

    public boolean save(ProviderEntity providerEntity) {
        if (providerEntity != null && providerEntity.getMail() != null) {
            ProviderEntity existingProvider = providerRepo.findByMail(providerEntity.getMail());
            if (existingProvider != null) {
                if (existingProvider.isActivo()) {
                    return false;
                }
                existingProvider.setActivo(true);
                providerRepo.save(existingProvider);
                return true;
            }
        }else {
            throw new IllegalArgumentException("ProviderEntity or mail cannot be null");
        }
        providerRepo.save(providerEntity);
        return true;
    }

    public List<ProviderEntity> findAll() {
        return providerRepo.findAll()
                .stream()
                .toList();
    }

    public List<ProviderEntity> findALlActive(){
        return providerRepo.findAll()
                .stream()
                .filter(ProviderEntity::isActivo)
                .toList();
    }

    public Page<ProviderEntity> findAllPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return providerRepo.findAll(pageable);
    }

    public Page<ProviderEntity> findAllPaginatedActive(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return providerRepo.findAllByActivo(pageable);
    }

    public ProviderEntity findById(long id) {
            return providerRepo.findById(id).orElseThrow(ProviderNotFoundException::new);
    }

    public List<ProviderEntity> findByName(String name) {
        return new ArrayList<>(providerRepo.findByName(name));
    }

    public ProviderEntity findByRazonSocial(String razonSocial){
        return providerRepo.findByRazonSocial(razonSocial);
    }

    public ProviderEntity findByCuit(String cuit){
        return providerRepo.findByCuit(cuit);
    }

    public void delete(ProviderEntity provider) {
        providerRepo.delete(provider);
    }

    public void update(ProviderEntity provider) {   //TODO resolver bien como actualizar el proveedor
        providerRepo.save(provider);
    }

    public void logicalDelete(ProviderEntity provider) {
        provider.setActivo(false);
        update(provider);
    }

    /* TODO Gestión de productos. arreglar con el repo de productos

        listarProductosDelProveedor(proveedorId)

        agregarProductoAlProveedor(proveedorId, productoData)

        eliminarProductoDelProveedor(proveedorId, productoId)

    */
    /* TODO ---Gestión de órdenes de compra

            crearOrdenDeCompra(proveedorId, datosOrden)

            listarOrdenesDeCompra(proveedorId, ordenId)

            obtenerOrdenDeCompra(ordenId)

            actualizarEstadoOrden(ordenId, estado)

      TODO ---Historial

            obtenerHistorialDeCompras(proveedorId)

            calcularTotalCompradoAProveedor(proveedorId, periodo)
    */

}
