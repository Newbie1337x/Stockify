package org.stockify.model.service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.ProviderRequest;
import org.stockify.dto.response.ProductResponse;
import org.stockify.dto.response.ProviderResponse;
import org.stockify.model.entity.ProviderEntity;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.ProviderMapper;
import org.stockify.model.repository.ProviderRepository;

@Service
public class ProviderService {

    private final ProviderRepository providerRepository;
    private final ProviderMapper providerMapper;

    public ProviderService(ProviderRepository providerRepository, ProviderMapper providerMapper) {
        this.providerRepository = providerRepository;
        this.providerMapper = providerMapper;

    }

    //---Crud operations---

    public ProviderResponse save(ProviderRequest providerRequest) {
        return providerMapper
                .toResponseDTO(providerRepository.save
                        (providerMapper.toEntity(providerRequest)));
    }

    public Page<ProviderResponse> findAll(Pageable pageable) {
        Page<ProviderEntity> page = providerRepository.findAll(pageable);
        return page.map(providerMapper::toResponseDTO);
    }

    public ProviderResponse findById(long id) {
        ProviderEntity provider = providerRepository.findById(id).orElseThrow(()-> new NotFoundException("Provider with ID " + id + " not found") );
        return providerMapper.toResponseDTO(provider);
    }

    public Page<ProviderResponse> findByName(Pageable pageable,String name)
    {
        Page<ProviderEntity> page = providerRepository.findByName(pageable,name);
        return page.map(providerMapper::toResponseDTO);
    }

    public ProviderResponse findByBusinessName(String businessName) {
        return providerMapper.toResponseDTO(providerRepository.findByBusinessName(businessName));
    }

    public ProviderResponse findByTaxId(String taxId) {
        return providerMapper.toResponseDTO(providerRepository.findByTaxId(taxId));
    }


    public void update(ProviderEntity provider) {   //TODO resolver bien como actualizar el proveedor
        providerRepository.save(provider);
    }

    public ProviderResponse logicalDelete(Long id) {
        ProviderEntity provider = providerRepository.findById(id).orElseThrow(()-> new NotFoundException("Provider with ID " + id + " not found"));
        provider.setActive(false);
        return providerMapper.toResponseDTO(providerRepository.save(provider));
    }

    public void delete(Long id) {
        providerRepository.deleteById(id);
    }




      // public List<ProductResponse> listarProductosDelProveedor(int providerID) {

       // return productRepository.findProductEntitiesByProviderid


        }

     //   agregarProductoAlProveedor(proveedorId, productoData)

       // eliminarProductoDelProveedor(proveedorId, productoId)


    /* TODO ---Gestión de órdenes de compra

            crearOrdenDeCompra(proveedorId, datosOrden)

            listarOrdenesDeCompra(proveedorId, ordenId)

            obtenerOrdenDeCompra(ordenId)

            actualizarEstadoOrden(ordenId, estado)

      TODO ---Historial

            obtenerHistorialDeCompras(proveedorId)

            calcularTotalCompradoAProveedor(proveedorId, periodo)
    */

