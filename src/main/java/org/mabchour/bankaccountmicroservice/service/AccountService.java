package org.mabchour.bankaccountmicroservice.service;

import org.mabchour.bankaccountmicroservice.dtos.BankAccountRequestDTO;
import org.mabchour.bankaccountmicroservice.dtos.BankAccountResponseDTO;

public interface AccountService {
    BankAccountResponseDTO addAccount(BankAccountRequestDTO bankAccountDTO);
}
