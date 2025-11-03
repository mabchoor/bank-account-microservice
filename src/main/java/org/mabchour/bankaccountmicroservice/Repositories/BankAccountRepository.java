package org.mabchour.bankaccountmicroservice.Repositories;

import org.mabchour.bankaccountmicroservice.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
}
