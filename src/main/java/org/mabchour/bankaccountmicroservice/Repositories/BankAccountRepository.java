package org.mabchour.bankaccountmicroservice.Repositories;

import org.mabchour.bankaccountmicroservice.entities.BankAccount;
import org.mabchour.bankaccountmicroservice.enums.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import org.mabchour.bankaccountmicroservice.entities.AccountSummaryProjection;
import org.mabchour.bankaccountmicroservice.entities.AccountDetailProjection;
import org.mabchour.bankaccountmicroservice.entities.AccountCurrencyProjection;

@RepositoryRestResource
public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
    @RestResource(path = "/byType")
    List<BankAccount> findByType(@Param("type") AccountType type);

    // Return a summary projection for all accounts
    @RestResource(path = "/summaries")
    List<AccountSummaryProjection> findAllProjectedBy();

    // Return a detailed projection for all accounts
    @RestResource(path = "/details")
    List<AccountDetailProjection> findAllDetailsProjectedBy();

    // Return accounts projected to currency info filtered by currency code
    @RestResource(path = "/byCurrency")
    List<AccountCurrencyProjection> findByCurrency_CurrencyCode(@Param("currency") String currency);
}
