package org.stockify.model.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.sale.SaleFilterRequest;
import org.stockify.dto.request.sale.SaleRequest;
import org.stockify.dto.response.SaleResponse;
import org.stockify.dto.response.TransactionResponse;
import org.stockify.model.entity.SaleEntity;
import org.stockify.model.entity.TransactionEntity;
import org.stockify.model.enums.TransactionType;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.SaleMapper;
import org.stockify.model.mapper.TransactionMapper;
import org.stockify.model.repository.ClientRepository;
import org.stockify.model.repository.SaleRepository;
import org.stockify.model.repository.TransactionRepository;
import org.stockify.model.specification.SaleSpecification;

@Service
@RequiredArgsConstructor

public class SaleService {

    private final StockService stockService;
    private final TransactionService transactionService;
    private final SaleMapper saleMapper;
    private final TransactionRepository transactionRepository;
    private final ClientRepository clientRepository;
    private final SaleRepository saleRepository;
    private final TransactionMapper transactionMapper;


    /**
     * Crea una nueva venta en el sistema y actualiza el stock de los productos.
     * 
     * @param request DTO con los datos de la venta a crear
     * @param storeID ID de la tienda donde se realiza la venta
     * @param posID ID del punto de venta donde se realiza la venta
     * @return DTO con los datos de la venta creada
     * @throws NotFoundException si no se encuentra el cliente o la transacción
     */
    @Transactional
    public SaleResponse createSale(SaleRequest request, long storeID, long posID){
        request.getTransaction().getDetailTransactions()
                .forEach(detail -> stockService.decreaseStock(detail.getProductID(), storeID, detail.getQuantity()));

        TransactionResponse transaction = transactionService.createTransaction(request.getTransaction(), storeID, posID, TransactionType.SALE);

        SaleEntity sale = saleMapper.toEntity(request);
        if (request.getClientId() != null) {
            sale.setClient(clientRepository.findById(request.getClientId()).orElseThrow(() -> new NotFoundException("Client not found with ID"+ request.getClientId())));
        }
        sale.setTransaction(transactionRepository.findById(transaction.getId())
                .orElseThrow(() -> new NotFoundException("Transaction not found")));


        return saleMapper.toResponseDTO(saleRepository.save(sale));
    }

    /**
     * Elimina una venta por su ID.
     * 
     * @param id ID de la venta a eliminar
     * @throws NotFoundException si no se encuentra ninguna venta con el ID especificado
     */
    public void delete (Long id) {
        if(!saleRepository.existsById(id)){
            throw new NotFoundException("Sale with ID " + id + " not found");
        }
        saleRepository.deleteById(id);
    }

    /**
     * Busca ventas aplicando filtros y paginación.
     * 
     * @param filterRequest DTO con los filtros a aplicar (ID de cliente, ID de venta, ID de transacción)
     * @param pageable Información de paginación
     * @return Página de ventas que cumplen con los filtros
     */
    public Page<SaleResponse> findAll(SaleFilterRequest filterRequest, Pageable pageable){
        Specification<SaleEntity> specification = Specification
                .where(SaleSpecification.byClientId(filterRequest.getClientId()))
                .and(SaleSpecification.bySaleId(filterRequest.getSaleId()))
                .and(SaleSpecification.byTransactionId(filterRequest.getTransactionId()));

        Page<SaleEntity> saleEntities = saleRepository.findAll(specification, pageable);
        return saleEntities.map(saleMapper::toResponseDTO);
    }

    /**
     * Busca una venta por su ID.
     * 
     * @param id ID de la venta a buscar
     * @return DTO con los datos de la venta encontrada
     * @throws NotFoundException si no se encuentra ninguna venta con el ID especificado
     */
    public SaleResponse findbyId(Long id) {
        SaleEntity saleEntity = saleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sale with ID " + id + " not found"));
        return saleMapper.toResponseDTO(saleEntity);
    }

    /**
     * Busca la transacción asociada a una venta.
     * 
     * @param saleId ID de la venta de la que se busca la transacción
     * @return DTO con los datos de la transacción asociada a la venta
     * @throws NotFoundException si no se encuentra ninguna venta con el ID especificado
     */
    public TransactionResponse findTransactionBySaleId(Long saleId) {
        SaleEntity saleEntity = saleRepository.findById(saleId)
                .orElseThrow(() -> new NotFoundException("Sale with ID " + saleId + " not found"));

        TransactionEntity transactionEntity = saleEntity.getTransaction();
        return transactionMapper.toDto(transactionEntity);
    }

    /**
     * Actualiza parcialmente una venta existente.
     * 
     * @param id ID de la venta a actualizar parcialmente
     * @param saleRequest DTO con los datos a actualizar de la venta
     * @return DTO con los datos de la venta actualizada
     * @throws NotFoundException si no se encuentra ninguna venta con el ID especificado
     */
    public SaleResponse updateShiftPartial(Long id, SaleRequest saleRequest){
        SaleEntity existingSale = saleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sale with ID " + id + " not found"));
        saleMapper.partialUpdateSaleEntity(saleRequest, existingSale);

        SaleEntity updatedSale = saleRepository.save(existingSale);
        return saleMapper.toResponseDTO(updatedSale);
    }

    /**
     * Actualiza completamente una venta existente.
     * 
     * @param id ID de la venta a actualizar
     * @param saleRequest DTO con los nuevos datos de la venta
     * @return DTO con los datos de la venta actualizada
     * @throws NotFoundException si no se encuentra ninguna venta con el ID especificado
     */
    public SaleResponse updateSaleFull(Long id, SaleRequest saleRequest){
        SaleEntity existingSale = saleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sale with ID " + id + " not found"));

        saleMapper.updateShiftEntity(saleRequest, existingSale);

        SaleEntity updatedSale = saleRepository.save(existingSale);
        return saleMapper.toResponseDTO(updatedSale);
    }
}
