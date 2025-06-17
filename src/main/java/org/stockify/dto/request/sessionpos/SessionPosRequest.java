package org.stockify.dto.request.sessionpos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Schema(name = "SessionPosRequest", description = "Request object for opening a new POS session with initial cash amount.")
public class SessionPosRequest {

    @Schema(description = "Initial amount when opening the POS session, must be zero or positive.", example = "1000.00")
    @NotNull(message = "Opening amount must not be null")
    @PositiveOrZero(message = "Opening amount must be zero or positive")
    private BigDecimal openingAmount;

}
