package org.stockify.model.service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.purchase.PurchaseFilterRequest;
import org.stockify.dto.request.purchase.PurchaseRequest;
import org.stockify.dto.response.PurchaseResponse;
import org.stockify.model.entity.PurchaseEntity;
import org.stockify.model.entity.TransactionEntity;
import org.stockify.model.enums.TransactionType;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.PurchaseMapper;
import org.stockify.model.repository.ProviderRepository;
import org.stockify.model.repository.PurchaseRepository;
import org.stockify.model.repository.TransactionRepository;
import org.stockify.model.specification.PurchaseSpecification;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseMapper purchaseMapper;
    private final StockService stockService;
    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;
    private final ProviderRepository providerRepository;


    //-- CRUD operations --

    /**
     * Crea una nueva compra en el sistema y actualiza el stock de los productos.
     * 
     * @param request DTO con los datos de la compra a crear
     * @param storeID ID de la tienda donde se realiza la compra
     * @param posID ID del punto de venta donde se realiza la compra
     * @return DTO con los datos de la compra creada
     * @throws NotFoundException si no se encuentra la transacción o el proveedor
     */
    @Transactional
    public PurchaseResponse createPurchase(PurchaseRequest request, Long storeID, Long posID) {

        request.getTransaction().getDetailTransactions()
                .forEach(detail -> stockService.increaseStock(detail.getProductID(), storeID, detail.getQuantity()));

        TransactionEntity transaction = transactionService.createTransaction(request.getTransaction(), storeID, posID, TransactionType.PURCHASE);
        PurchaseEntity purchase = purchaseMapper.toEntity(request);

        purchase.setTransaction(transactionRepository.findById(transaction.getId())
                .orElseThrow(() -> new NotFoundException("Transaction not found")));

        purchase.setProvider(providerRepository.findById(request.getProviderId()).orElseThrow(() -> new NotFoundException("Provider not found")));

        return purchaseMapper.toResponseDTO(purchaseRepository.save(purchase));
    }

    /**
     * Actualiza una compra existente.
     * 
     * @param id ID de la compra a actualizar
     * @param request DTO con los nuevos datos de la compra
     * @return DTO con los datos de la compra actualizada
     */
    public PurchaseResponse updatePurchase(Long id, PurchaseRequest request) {
        PurchaseEntity purchaseEntity = purchaseMapper.toEntity(request);
        purchaseEntity.setId(id);
        return purchaseMapper.toResponseDTO(purchaseRepository.save(purchaseEntity));
    }

    /**
     * Elimina una compra por su ID.
     * 
     * @param id ID de la compra a eliminar
     */
    public void deletePurchase(Long id) {
        purchaseRepository.deleteById(id);
    }

    /**
     * Busca compras aplicando filtros y paginación.
     * 
     * @param pageable Información de paginación
     * @param request DTO con los filtros a aplicar (ID de transacción, ID de proveedor, ID de compra)
     * @return Página de compras que cumplen con los filtros
     */
    public Page<PurchaseResponse> getAllPurchases(Pageable pageable, PurchaseFilterRequest request) {
        Specification<PurchaseEntity> spec = Specification
                .where(PurchaseSpecification.ByTransactionId(request.getTransactionId()))
                .and(PurchaseSpecification.ByProviderId(request.getProviderId()))
                .and(PurchaseSpecification.ByPurchaseId(request.getPurchaseId()));
        return purchaseRepository.findAll(spec, pageable)
                .map(purchaseMapper::toResponseDTO);
    }


    /**
     * Busca una compra por su ID.
     *
     * @param id ID de la compra a buscar
     * @return DTO con los datos de la compra encontrada
     * @throws NotFoundException si no se encuentra la compra
     */
    public PurchaseResponse findById(Long id) {
        PurchaseEntity purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Purchase not found with id: " + id));
        return purchaseMapper.toResponseDTO(purchase);
    }
}
