package org.stockify.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.stockify.dto.response.TransactionPDFResponse;
import org.stockify.dto.response.TransactionResponse;
import org.stockify.model.entity.TransactionEntity;
import org.stockify.model.enums.TransactionType;
import org.stockify.model.exception.NotFoundException;
import org.stockify.model.exception.TypeNotAcceptedException;
import org.stockify.model.mapper.TransactionMapper;
import org.stockify.model.repository.TransactionRepository;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * Service responsible for generating PDF documents from transaction data.
 * This service fetches transaction details, maps them to DTOs, processes HTML templates using Thymeleaf,
 * and converts the resulting HTML into a PDF file stored in the user's home directory.
 * It currently supports PDF generation only for PURCHASE and SALE transaction types.
 *
 */
@Service
@RequiredArgsConstructor
public class PdfGeneratorService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    /**
     *
     * Generates a PDF file for a transaction identified by its ID.
     *
     * <p>
     * The method fetches the transaction entity from the database, verifies if its type is supported,
     * then generates the corresponding PDF using an HTML template rendered with Thymeleaf.
     * The resulting PDF file is saved to the user's home directory with a filename pattern:
     * transaction_{id}.pdf.
     * </p>
     *
     * @param id the unique identifier of the transaction
     * @return a {@link ResponseEntity} containing an {@link EntityModel} with a {@link TransactionPDFResponse}
     *         that includes the path to the generated PDF and transaction data
     * @throws NotFoundException if the transaction does not exist, or if the transaction type is not supported for PDF generation
     * @throws Exception         if any error occurs during the PDF generation process
     */
    public ResponseEntity<EntityModel<TransactionPDFResponse>> generatePdf(Long id) throws Exception {
        TransactionEntity transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction with ID " + id + " not found."));

        // Map entity to DTO for template rendering
        TransactionResponse dto = transactionMapper.toDto(transaction);

        // Define an output PDF file path in the user's home directory
        String fileName = "transaction_" + id + ".pdf";
        String outputPath = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath() + File.separator + fileName;

        // Only PURCHASE and SALE types supported for PDF generation
        if (transaction.getType() == TransactionType.PURCHASE || transaction.getType() == TransactionType.SALE) {
            generateHtmlToPdf(dto, outputPath, transaction);
        } else {
            throw new TypeNotAcceptedException("Transaction type not supported for PDF generation.");
        }

        // Return response with PDF file path and transaction details
        return ResponseEntity.ok(EntityModel.of(
                TransactionPDFResponse.builder()
                        .path(outputPath)
                        .transaction(dto)
                        .build()));
    }

    /**
     * Generates a PDF from HTML using Thymeleaf templates.
     *
     * Converts transaction data into an HTML string using Thymeleaf templates and then generates a PDF file.
     * <p>
     * This method sets up Thymeleaf with a classpath template resolver, creates a context with transaction data,
     * processes either a purchase or sale HTML template,
     * and renders it to a PDF file using the Flying Saucer library.
     * @param dto          the transaction data transfer object containing transaction details for the template
     * @param outputPath   the file system path where the generated PDF should be saved
     * @param transaction  the original transaction entity, used to determine a transaction type and related details
     * @throws Exception if any IO or rendering error occurs during PDF generation
     */
    private void generateHtmlToPdf(TransactionResponse dto, String outputPath, TransactionEntity transaction) throws Exception {
        // Configure Thymeleaf template resolver for HTML files with UTF-8 encoding
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode("HTML");
        resolver.setPrefix("");
        resolver.setSuffix(".html");
        resolver.setCharacterEncoding("UTF-8");

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);

        // Prepare Thymeleaf context with transaction variables
        Context context = new Context();

        // Comprovar si es una compra o una venta y completar el contexto
        if (transaction.getType() == TransactionType.PURCHASE && transaction.getPurchase() != null) {
            String providerName = transaction.getPurchase().getProvider().getBusinessName();
            context.setVariable("providerName", providerName);
            context.setVariable("transactionId", dto.getId());
            context.setVariable("date", dto.getDateTime());
            context.setVariable("total", dto.getTotal());
        } else {
            String customerName = transaction.getSale().getClient().getFirstName() + " " + transaction.getSale().getClient().getLastName();
            context.setVariable("customerName", customerName);
            context.setVariable("transactionId", dto.getId());
            context.setVariable("storeName", dto.getStoreName());
            context.setVariable("pos", dto.getIdPos());
            context.setVariable("date", dto.getDateTime());
            context.setVariable("total", dto.getTotal());
        }

        // Prepare transaction detail items as a list of maps for the template
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

        // Select a template based on a transaction type
        String templateName = transaction.getType() == TransactionType.PURCHASE ? "buyTemplate" : "saleTemplate";

        // Process HTML content
        String html = templateEngine.process(templateName, context);

        // Render HTML to PDF using Flying Saucer (ITextRenderer)
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();

        try (FileOutputStream os = new FileOutputStream(outputPath)) {
            renderer.createPDF(os);
        }
    }
}
