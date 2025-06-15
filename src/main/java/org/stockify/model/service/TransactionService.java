package org.stockify.model.service;

import jakarta.transaction.Transactional;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.stockify.dto.request.transaction.TransactionCreatedRequest;
import org.stockify.dto.request.transaction.TransactionRequest;
import org.stockify.dto.response.TransactionCreatedResponse;
import org.stockify.dto.response.TransactionPDFResponse;
import org.stockify.dto.response.TransactionResponse;
import org.stockify.model.entity.DetailTransactionEntity;
import org.stockify.model.entity.ProductEntity;
import org.stockify.model.entity.StoreEntity;
import org.stockify.model.entity.TransactionEntity;
import org.stockify.model.enums.TransactionType;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.mapper.TransactionMapper;
import org.stockify.model.repository.ProductRepository;
import org.stockify.model.repository.StoreRepository;
import org.stockify.model.repository.TransactionRepository;
import org.stockify.model.repository.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final PosRepository posRepository;
    private final SessionPosRepository sessionPosRepository;

    public TransactionService(TransactionRepository transactionRepository, TransactionMapper transactionMapper, StoreRepository storeRepository, PosRepository posRepository, ProductRepository productRepository, SessionPosRepository sessionPosRepository) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
        this.posRepository = posRepository;
        this.sessionPosRepository = sessionPosRepository;
    }

    /**
     * Creates and saves a monetary transaction without product details.
     * This is typically used for simple financial operations such as cash withdrawals,
     * paying external services (e.g., taxis), or recording generic money movements.
     *
     * @param request the basic transaction data
     * @param idLocal the ID of the store where the transaction occurs
     * @param idPos the ID of the POS terminal used
     * @param type the type of transaction (e.g., CASH_OUT, TRANSFER)
     * @return a response summarizing the created transaction
     */
    public TransactionCreatedResponse saveTransaction(TransactionCreatedRequest request, Long idLocal, Long idPos, TransactionType type) {
        if (!posRepository.existsById(idPos)) {
            throw new NotFoundException("POS with ID " + idPos + " not found.");
        }
        TransactionEntity transactionEntity = transactionMapper.toEntity(request);
        transactionEntity.setTotal(request.getTotalAmount());

        // Setear la sesión del POS
        transactionEntity.setSessionPosEntity(
                sessionPosRepository.findByPosEntity_IdAndCloseTime
                        (idPos, null).orElseThrow
                        (() -> new NotFoundException("POS with ID " + idPos + " not found."))
        );
        // Buscar y setear el local
        StoreEntity store = storeRepository.findById(idLocal)
                .orElseThrow(() -> new NotFoundException("Store with ID " + idLocal + " not found."));
        transactionEntity.setStore(store);
        transactionEntity.setDescription(request.getDescription());
        transactionEntity.setType(type);

        return transactionMapper.toDtoCreated(transactionRepository
                .save(transactionEntity));
    }

    /**
     * Creates and persists a complete transaction with product details.
     * This is used specifically by sales and purchase services to register operations
     * that involve specific products, their quantities, and prices.
     *
     * @param request the detailed transaction request including products and quantities
     * @param idLocal the ID of the store associated with the transaction
     * @param idPos the ID of the POS terminal involved
     * @param type the type of transaction (SALE or PURCHASE)
     * @return the persisted TransactionEntity
     */
    public TransactionEntity createTransaction(TransactionRequest request, Long idLocal, Long idPos, TransactionType type) {

        if (!posRepository.existsById(idPos)) {
            throw new NotFoundException("POS with ID " + idPos + " not found.");
        }

        // Convertir cada detalle a entidad, seteando producto, cantidad, subtotal
        Set<DetailTransactionEntity> detailTransactions = request
                .getDetailTransactions()
                .stream()
                .map(detailRequest -> {
                    ProductEntity product = productRepository.findById(detailRequest.getProductID())
                            .orElseThrow(() -> new NotFoundException("Product with ID " + detailRequest.getProductID() + " not found."));

                    DetailTransactionEntity entity = new DetailTransactionEntity();
                    entity.setProduct(product);

                    BigDecimal quantity = BigDecimal.valueOf(detailRequest.getQuantity());
                    entity.setQuantity(quantity);

                    entity.setSubtotal(product.getPrice().multiply(quantity));

                    return entity;
                })
                .collect(Collectors.toSet());

        // Crear la transacción base
        TransactionEntity transactionEntity = transactionMapper.toEntity(request);
        transactionEntity.setDetailTransactions(detailTransactions);


        detailTransactions.forEach
                (detail -> detail.setTransaction(transactionEntity));

        // Setear la sesión del POS
        transactionEntity.setSessionPosEntity(
                sessionPosRepository.findByPosEntity_IdAndCloseTime
                        (idPos, null).orElseThrow
                        (() -> new NotFoundException("POS with ID " + idPos + " not found."))
        );

        // Buscar y setear el local
        StoreEntity store = storeRepository.findById(idLocal)
                .orElseThrow(() -> new NotFoundException("Store with ID " + idLocal + " not found."));
        transactionEntity.setStore(store);

        // Calcular el total
        transactionEntity.setTotal(
                detailTransactions
                        .stream()
                        .map(DetailTransactionEntity::getSubtotal)
                        .reduce(BigDecimal::add)
                        .orElse(BigDecimal.ZERO)
        );

        transactionEntity.setDescription(request.getDescription());
        transactionEntity.setType(type);

        // Guardar y devolver el response
        return transactionRepository.save(transactionEntity);
    }


    public List<TransactionResponse> findAll()
    {
        return transactionRepository
                .findAll()
                .stream()
                .map(transactionMapper::toDto).toList();
    }


    public ResponseEntity<EntityModel<TransactionPDFResponse>> generatePdf(Long id) throws Exception {
        TransactionEntity transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction with ID " + id + " not found."));

        // Convertir a DTO
        TransactionResponse dto = transactionMapper.toDto(transaction);



        // Generar PDF en el siguiente directorio
        String fileName = "transaction_" + id + ".pdf";
        String outputPath = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath() + File.separator + fileName;

        // Verificar si es una compra o una venta
        if (transaction.getType() == TransactionType.PURCHASE || transaction.getType() == TransactionType.SALE ) {
            generateHtmlToPdf(dto, outputPath, transaction);
        } else {
            throw new NotFoundException("Transaction type not supported for PDF generation.");
        }

        // Retornar la respuesta con el PDF generado
        return ResponseEntity.ok(EntityModel.of(
                TransactionPDFResponse.builder()
                        .path(outputPath)
                        .transaction(dto).build()));
    }


    private void generateHtmlToPdf(TransactionResponse dto, String outputPath, TransactionEntity transaction) throws Exception {
        // Configurar Thymeleaf
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode("HTML");
        resolver.setPrefix("");
        resolver.setSuffix(".html");
        resolver.setCharacterEncoding("UTF-8");

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);

        // Preparar el contexto
        Context context = new Context();

        // Comprovar si es una compra o una venta y completar el contexto

        if (transaction.getType() == TransactionType.PURCHASE && transaction.getPurchase() != null) {
            String providerName = transaction.getPurchase().getProvider().getBusinessName();

            context.setVariable("providerName", providerName);
            context.setVariable("transactionId", dto.getId());
            context.setVariable("date", dto.getDateTime());
            context.setVariable("total", dto.getTotal());
        }else {
            String customerName = "";
            context.setVariable("customerName", customerName);
            context.setVariable("transactionId", dto.getId());
            context.setVariable("storeName", dto.getStoreName());
            context.setVariable("pos", dto.getIdPos());
            context.setVariable("date", dto.getDateTime());
            context.setVariable("total", dto.getTotal());
        }

        // Convertir los detalles de transacción a un formato adecuado para la plantilla
        List<Map<String, Object>> items = dto.getDetailTransactions().stream()
                .map(detail -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("description", detail.getProduct().getName());
                    item.put("quantity", detail.getQuantity());
                    item.put("price", detail.getProduct().getPrice());
                    return item;
                })
                .collect(Collectors.toList());
        context.setVariable("items", items);

        // Usar buyTemplate.html para compras
        String templateName = transaction.getType() == TransactionType.PURCHASE ? "buyTemplate" : "saleTemplate";
        String html = templateEngine.process(templateName, context);

        // Convertir HTML a PDF
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();

        try (FileOutputStream os = new FileOutputStream(outputPath)) {
            renderer.createPDF(os);
        }
    }


}
