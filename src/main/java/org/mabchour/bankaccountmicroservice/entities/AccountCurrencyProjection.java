package org.mabchour.bankaccountmicroservice.entities;

import org.springframework.data.rest.core.config.Projection;
import org.mabchour.bankaccountmicroservice.entities.BankAccount;

@Projection(name = "AccountCurrency", types = {BankAccount.class})
public interface AccountCurrencyProjection {
    String getId();
    String getCurrency();
}
