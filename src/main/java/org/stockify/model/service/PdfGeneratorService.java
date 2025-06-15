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

@Service
@RequiredArgsConstructor
public class PdfGeneratorService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    /**
     * Generates a PDF for a transaction by its ID.
     *
     * @param id the ID of the transaction
     * @return a ResponseEntity containing the path to the generated PDF
     * @throws Exception if an error occurs during PDF generation
     */
    public ResponseEntity<EntityModel<TransactionPDFResponse>> generatePdf(Long id) throws Exception {
        TransactionEntity transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction with ID " + id + " not found."));

        // Convertir a DTO
        TransactionResponse dto = transactionMapper.toDto(transaction);

        // Generar PDF en el siguiente directorio
        String fileName = "transaction_" + id + ".pdf";
        String outputPath = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath() + File.separator + fileName;

        // Verificar si es una compra o una venta
        if (transaction.getType() == TransactionType.PURCHASE || transaction.getType() == TransactionType.SALE) {
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
        } else {
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
