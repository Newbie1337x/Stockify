package org.stockify.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.stockify.model.dto.PurchaseAuditDTO;
import org.stockify.model.dto.SaleAuditDTO;
import org.stockify.model.dto.TransactionAuditDTO;
import org.stockify.model.service.AuditService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/audit")
public class AuditController {
    //Implement Hateoas and REST
    private final AuditService auditService;


    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionAuditDTO>> getAllTransactionAudit() {
        return ResponseEntity.ok(auditService.getAllTransactionAudits());
    }

    @GetMapping("/purchases")
    public ResponseEntity<List<PurchaseAuditDTO>> getAllPurchaseAudit() {
        return ResponseEntity.ok(auditService.getAllPurchaseAudits());
    }

    @GetMapping("/sales")
    public ResponseEntity<List<SaleAuditDTO>> getAllSaleAudit() {
        return ResponseEntity.ok(auditService.getAllSaleAudits());
    }
}
