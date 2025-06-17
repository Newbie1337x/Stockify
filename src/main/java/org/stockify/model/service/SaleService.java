package org.stockify.model.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.sale.SaleFilterRequest;
import org.stockify.dto.request.sale.SaleRequest;
import org.stockify.dto.response.SaleResponse;
import org.stockify.dto.response.TransactionResponse;
import org.stockify.model.entity.EmployeeEntity;
import org.stockify.model.entity.PosEntity;
import org.stockify.model.entity.SaleEntity;
import org.stockify.model.entity.TransactionEntity;
import org.stockify.model.enums.Status;
import org.stockify.model.enums.TransactionType;
import org.stockify.model.exception.InvalidSessionStatusException;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.SaleMapper;
import org.stockify.model.mapper.TransactionMapper;
import org.stockify.model.repository.ClientRepository;
import org.stockify.model.repository.PosRepository;
import org.stockify.model.repository.SaleRepository;
import org.stockify.model.specification.SaleSpecification;
import org.stockify.security.model.entity.CredentialsEntity;
import org.stockify.security.repository.CredentialRepository;
import org.stockify.security.service.JwtService;

/**
 * Service class that handles the business logic related to sales.
 * This includes creating, updating, retrieving, and deleting sales,
 * as well as stock management and transaction association.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class SaleService {

    private final StockService stockService;
    private final TransactionService transactionService;
    private final SaleMapper saleMapper;
    private final ClientRepository clientRepository;
    private final SaleRepository saleRepository;
    private final TransactionMapper transactionMapper;
    private final PosService posService;
    private final PosRepository posRepository;
    private final SessionPosService sessionPosService;
    private final  JwtService jwtService;
    private final CredentialRepository credentialsRepository;

    /**
     * Creates a new sale in the system and updates stock accordingly.
     * It first validates if the POS exists and is open.
     * Then, it decreases stock for all products involved in the transaction,
     * creates the transaction, associates the client (if provided),
     * updates the POS session amount, and saves the sale.
     *
     * @param request DTO containing the sale data to be created
     * @param posID   ID of the POS where the sale is taking place
     * @return {@link SaleResponse} containing the saved sale information
     * @throws NotFoundException              if the POS or client is not found
     * @throws InvalidSessionStatusException if the POS session is not open
     */
    public SaleResponse createSale(SaleRequest request, long posID) {
        // Use the centralized validation method from TransactionService
        PosEntity posEntity = transactionService.validatePosAndEmployee(posID);
        Long localId = posEntity.getStore().getId();

        // Decrease stock for each product in the sale
        request.getTransaction()
                .getDetailTransactions()
                .forEach(detail ->
                        stockService.decreaseStock(detail.getProductID(), localId, detail.getQuantity()));

        // Map sale request to entity and create associated transaction
        SaleEntity sale = saleMapper.toEntity(request);
        sale.setTransaction(
                transactionService.createTransaction(request.getTransaction(), localId, posID, TransactionType.SALE)
        );

        // Associate client if provided
            if (request.getClientId() != null) {
                sale.setClient(clientRepository.findById(request.getClientId())
                        .orElseThrow(() ->
                                new NotFoundException("Client not found with ID " + request.getClientId())));
        }

        // Update the POS session with the sale total
        posService.addAmount(posID, sale.getTransaction().getTotal());

        return saleMapper.toResponseDTO(saleRepository.save(sale));
    }

    /**
     * Deletes a sale by its ID.
     *
     * @param id the ID of the sale to delete
     * @throws NotFoundException if the sale with the given ID does not exist
     */
    public void delete(Long id) {
        if (!saleRepository.existsById(id)) {
            throw new NotFoundException("Sale with ID " + id + " not found");
        }
        saleRepository.deleteById(id);
    }

    /**
     * Retrieves all sales matching the provided filter criteria, with pagination support.
     *
     * @param filterRequest the filter criteria for the search (client ID, sale ID, transaction ID)
     * @param pageable      pagination information
     * @return a paginated list of {@link SaleResponse} objects matching the criteria
     */
    public Page<SaleResponse> findAll(SaleFilterRequest filterRequest, Pageable pageable) {
        Specification<SaleEntity> specification = Specification
                .where(SaleSpecification.byClientId(filterRequest.getClientId()))
                .and(SaleSpecification.bySaleId(filterRequest.getSaleId()))
                .and(SaleSpecification.byTransactionId(filterRequest.getTransactionId()));

        Page<SaleEntity> saleEntities = saleRepository.findAll(specification, pageable);
        return saleEntities.map(saleMapper::toResponseDTO);
    }

    /**
     * Finds a specific sale by its ID.
     *
     * @param id the ID of the sale to retrieve
     * @return {@link SaleResponse} containing sale details
     * @throws NotFoundException if no sale with the given ID exists
     */
    public SaleResponse findById(Long id) {
        SaleEntity saleEntity = saleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sale with ID " + id + " not found"));
        return saleMapper.toResponseDTO(saleEntity);
    }

    /**
     * Retrieves the transaction associated with a specific sale.
     *
     * @param saleId the ID of the sale
     * @return {@link TransactionResponse} containing transaction details
     * @throws NotFoundException if no sale or transaction is found for the given ID
     */
    public TransactionResponse findTransactionBySaleId(Long saleId) {
        SaleEntity saleEntity = saleRepository.findById(saleId)
                .orElseThrow(() -> new NotFoundException("Sale with ID " + saleId + " not found"));

        TransactionEntity transactionEntity = saleEntity.getTransaction();
        return transactionMapper.toDto(transactionEntity);
    }

    /**
     * Partially updates the fields of a sale with the given ID.
     *
     * @param id          the ID of the sale to update
     * @param saleRequest the request containing updated fields
     * @return {@link SaleResponse} containing the updated sale information
     * @throws NotFoundException if no sale with the given ID exists
     */
    public SaleResponse updateShiftPartial(Long id, SaleRequest saleRequest) {
        SaleEntity existingSale = saleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sale with ID " + id + " not found"));
        saleMapper.partialUpdateSaleEntity(saleRequest, existingSale);

        SaleEntity updatedSale = saleRepository.save(existingSale);
        return saleMapper.toResponseDTO(updatedSale);
    }

    /**
     * Fully updates a sale entity with the provided data.
     *
     * @param id          the ID of the sale to update
     * @param saleRequest the request containing the full new sale data
     * @return {@link SaleResponse} containing the updated sale details
     * @throws NotFoundException if no sale with the given ID exists
     */
    public SaleResponse updateSaleFull(Long id, SaleRequest saleRequest) {
        SaleEntity existingSale = saleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sale with ID " + id + " not found"));

        saleMapper.updateShiftEntity(saleRequest, existingSale);

        SaleEntity updatedSale = saleRepository.save(existingSale);
        return saleMapper.toResponseDTO(updatedSale);
    }
}
