package org.stockify.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.stockify.dto.response.TransactionPDFResponse;
import org.stockify.model.service.PdfGeneratorService;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "Pdf-Generator", description = "Operations related to PDF generation")
@SecurityRequirement(name = "bearerAuth")
public class PdfGeneratorController {

    private final PdfGeneratorService pdfGeneratorService;

    @Operation(summary = "Generate PDF for a transaction (purchase or sale only) by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PDF generated successfully"),
            @ApiResponse(responseCode = "404", description = "Transaction not found"),
            @ApiResponse(responseCode = "400", description = "Transaction type not supported for PDF generation")
    })
    @GetMapping("/transaction/pdf/{idTransaction}")
    @PreAuthorize("hasRole('ROLE_ADMIN') and hasAuthority('READ') or " +
            "hasRole('ROLE_MANAGER') and hasAuthority('READ')")
    public ResponseEntity<EntityModel<TransactionPDFResponse>> generatePdf(
            @Parameter(description = "ID of the transaction") @PathVariable Long idTransaction) throws Exception {

        return pdfGeneratorService.generatePdf(idTransaction);
    }
}
