package org.mabchour.bankaccountmicroservice.entities;

import org.mabchour.bankaccountmicroservice.enums.AccountType;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "AccountSummary", types = {BankAccount.class})
public interface AccountSummaryProjection {
    String getId();
    AccountType getType();
    java.math.BigDecimal getBalance();
    String getCurrency();
}

