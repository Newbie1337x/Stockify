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
import org.stockify.model.exception.InvalidSessionStatusException;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.SaleMapper;
import org.stockify.model.mapper.ShiftMapper;
import org.stockify.model.mapper.TransactionMapper;
import org.stockify.model.repository.ClientRepository;
import org.stockify.model.repository.PosRepository;
import org.stockify.model.repository.SaleRepository;
import org.stockify.model.repository.TransactionRepository;
import org.stockify.model.specification.SaleSpecification;

@Service
public class SaleService {

    private final StockService stockService;
    private final TransactionService transactionService;
    private final SaleMapper saleMapper;
    private final TransactionRepository transactionRepository;
    private final ClientRepository clientRepository;
    private final SaleRepository saleRepository;
    private final TransactionMapper transactionMapper;
    private final PosService posService;
    private final PosRepository posRepository;
    private final SessionPosService sessionPosService;

    public SaleService(StockService stockService, TransactionService transactionService, SaleMapper saleMapper, TransactionRepository transactionRepository, ClientRepository clientRepository, SaleRepository saleRepository, ShiftMapper shiftMapper, TransactionMapper transactionMapper, PosService posService, PosRepository posRepository, SessionPosService sessionPosService) {
        this.stockService = stockService;
        this.transactionService = transactionService;
        this.saleMapper = saleMapper;
        this.transactionRepository = transactionRepository;
        this.clientRepository = clientRepository;
        this.saleRepository = saleRepository;
        this.transactionMapper = transactionMapper;
        this.posService = posService;
        this.posRepository = posRepository;
        this.sessionPosService = sessionPosService;
    }

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

        if (!posRepository.existsById(posID)) {
            throw new NotFoundException("POS with ID " + posID + " not found.");
        }

        if (!sessionPosService.isOpened(posID,null))
        {
            throw new InvalidSessionStatusException
                    ("POS with ID " + posID + " is closed. Please open it before creating a sale.");
        }
        //reduce el stock de un producto en la tienda selecionada
        request.getTransaction()
                .getDetailTransactions()
                .forEach(detail ->
                        stockService.decreaseStock(detail.getProductID(), storeID, detail.getQuantity()));

        SaleEntity sale = saleMapper.toEntity(request);
        //Crea la transaccion
        sale.setTransaction(
                transactionService
                        .createTransaction
                                (request.getTransaction(), storeID, posID, TransactionType.SALE));

        if (request.getClientId() != null) {
            sale.setClient(clientRepository
                    .findById(request.getClientId())
                    .orElseThrow(() ->
                            new NotFoundException("Client not found with ID"+ request.getClientId())));
        }
        posService.addAmount(posID, sale.getTransaction().getTotal());


        return saleMapper.toResponseDTO(saleRepository.save(sale));
    }

    public void delete (Long id) {
        if(!saleRepository.existsById(id)){
            throw new NotFoundException("Sale with ID " + id + " not found");
        }
        saleRepository.deleteById(id);
    }

    public Page<SaleResponse> findAll(SaleFilterRequest filterRequest, Pageable pageable){
        Specification<SaleEntity> specification = Specification
                .where(SaleSpecification.byClientId(filterRequest.getClientId()))
                .and(SaleSpecification.bySaleId(filterRequest.getSaleId()))
                .and(SaleSpecification.byTransactionId(filterRequest.getTransactionId()));

        Page<SaleEntity> saleEntities = saleRepository.findAll(specification, pageable);
        return saleEntities.map(saleMapper::toResponseDTO);
    }

    public SaleResponse findbyId(Long id) {
        SaleEntity saleEntity = saleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sale with ID " + id + " not found"));
        return saleMapper.toResponseDTO(saleEntity);
    }

    public TransactionResponse findTransactionBySaleId(Long saleId) {
        SaleEntity saleEntity = saleRepository.findById(saleId)
                .orElseThrow(() -> new NotFoundException("Sale with ID " + saleId + " not found"));

        TransactionEntity transactionEntity = saleEntity.getTransaction();
        return transactionMapper.toDto(transactionEntity);
    }

    public SaleResponse updateShiftPartial(Long id, SaleRequest saleRequest){
        SaleEntity existingSale = saleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sale with ID " + id + " not found"));
        saleMapper.partialUpdateSaleEntity(saleRequest, existingSale);

        SaleEntity updatedSale = saleRepository.save(existingSale);
        return saleMapper.toResponseDTO(updatedSale);
    }

    public SaleResponse updateSaleFull(Long id, SaleRequest saleRequest){
        SaleEntity existingSale = saleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sale with ID " + id + " not found"));

        saleMapper.updateShiftEntity(saleRequest, existingSale);

        SaleEntity updatedSale = saleRepository.save(existingSale);
        return saleMapper.toResponseDTO(updatedSale);
    }
}
