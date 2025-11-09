package org.mabchour.bankaccountmicroservice.entities;

import org.mabchour.bankaccountmicroservice.enums.AccountType;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "AccountProjection", types = {BankAccount.class})
public interface AccountProjection {
    public String getId();
    public AccountType getType();
}
