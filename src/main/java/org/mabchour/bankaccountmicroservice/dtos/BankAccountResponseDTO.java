package org.mabchour.bankaccountmicroservice.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data @NoArgsConstructor
@AllArgsConstructor @Builder
public class BankAccountResponseDTO {
    private String id;
    private Long createdAt;
    private BigDecimal balance;
    private String currency;
    private String type;
}
