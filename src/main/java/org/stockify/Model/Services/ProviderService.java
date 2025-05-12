package org.stockify.Model.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stockify.Model.Entities.ProviderEntity;
import org.stockify.Model.Exceptions.ProviderNotFoundException;
import org.stockify.Model.Repositories.ProviderRepo;

import java.util.List;

@Service
public class ProviderService {

    @Autowired
    private ProviderRepo providerRepo;

    //---Crud operations---

    public boolean save(ProviderEntity providerEntity) {
        if (providerEntity.getId() != null) {
            ProviderEntity existingProvider = providerRepo.findById(providerEntity.getId()).orElse(null);
            if (existingProvider != null) {
                if (!existingProvider.isActivo()) {
                    existingProvider.setActivo(true);
                    providerRepo.save(existingProvider);
                    return true;
                }
                return false;
            }
        }
        providerRepo.save(providerEntity);
        return true;
    }

    public List<ProviderEntity> findAll() {
        return providerRepo.findAll()
                .stream()
                .filter(ProviderEntity::isActivo)
                .toList();
    }

    public ProviderEntity findById(long id) {
        return providerRepo.findById(id).orElseThrow(ProviderNotFoundException::new);
    }

    public ProviderEntity findByName(String name){
        return providerRepo.findByName(name);
    }

    public ProviderEntity findByEmail(String email){
        return providerRepo.findByEmail(email);
    }

    public ProviderEntity findByRazonSocial(String razonSocial){
        return providerRepo.findByRazonSocial(razonSocial);
    }

    public ProviderEntity findByCUIT(String cuit){
        return providerRepo.findByCUIT(cuit);
    }

    public ProviderEntity findByDireccionFiscal(String direccionFiscal){
        return providerRepo.findByDireccionFiscal(direccionFiscal);
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
