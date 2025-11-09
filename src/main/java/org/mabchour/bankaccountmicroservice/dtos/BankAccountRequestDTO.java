package org.mabchour.bankaccountmicroservice.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mabchour.bankaccountmicroservice.enums.AccountType;

import java.math.BigDecimal;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class BankAccountRequestDTO {

    private BigDecimal balance;
    private String currency;
    private AccountType type;
}
