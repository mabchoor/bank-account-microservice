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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccountDTO {
    private String id;
    // epoch millis
    private Long createdAt;

    @NotNull(message = "balance must be provided")
    @PositiveOrZero(message = "balance must be positive or zero")
    private BigDecimal balance;

    @NotNull(message = "currency must be provided")
    @Size(min = 3, max = 3, message = "currency must be a 3-letter ISO code")
    private String currency;

    @NotNull(message = "type must be provided")
    private AccountType type;
}
