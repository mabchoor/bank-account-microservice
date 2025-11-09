package org.mabchour.bankaccountmicroservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mabchour.bankaccountmicroservice.enums.AccountType;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class BankAccount {
    @Id
    private String id ;
    private Date createdAt;
    private BigDecimal balance;
    private Currency currency;
    @Enumerated(EnumType.STRING)
    private AccountType type;

}

