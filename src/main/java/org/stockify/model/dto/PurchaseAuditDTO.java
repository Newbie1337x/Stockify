package org.stockify.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.stockify.dto.response.TransactionResponse;

@Data
@Builder
@AllArgsConstructor
public class PurchaseAuditDTO {
    private Long revision;            // Revision number
    private String revisionType;      // Change type: ADD, MOD, DEL
    private Long id;
    private TransactionResponse transaction;
}