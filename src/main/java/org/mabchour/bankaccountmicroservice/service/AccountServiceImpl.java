package org.mabchour.bankaccountmicroservice.service;

import org.mabchour.bankaccountmicroservice.entities.BankAccount;
import org.springframework.transaction.annotation.Transactional;
import org.mabchour.bankaccountmicroservice.dtos.BankAccountRequestDTO;
import org.mabchour.bankaccountmicroservice.dtos.BankAccountResponseDTO;
import org.springframework.stereotype.Service;
import org.mabchour.bankaccountmicroservice.Repositories.BankAccountRepository;
import org.mabchour.bankaccountmicroservice.mappers.BankAccountMapper;
import org.mabchour.bankaccountmicroservice.dtos.BankAccountDTO;

@Service @Transactional
public class AccountServiceImpl implements AccountService {
    private final BankAccountRepository bankAccountRepository;

    public AccountServiceImpl(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }
    @Override
    public BankAccountResponseDTO addAccount(BankAccountRequestDTO bankAccountDTO) {
        // Convert request DTO to the mapper's DTO, then to entity
        BankAccountDTO dto = BankAccountDTO.builder()
                .balance(bankAccountDTO.getBalance())
                .currency(bankAccountDTO.getCurrency())
                .type(bankAccountDTO.getType())
                .build();

        BankAccount bankAccount = BankAccountMapper.toEntity(dto);
        BankAccount saved = bankAccountRepository.save(bankAccount);

        // Return response DTO built from the saved entity
        return BankAccountResponseDTO.builder()
                .id(saved.getId())
                .createdAt(saved.getCreatedAt() != null ? saved.getCreatedAt().getTime() : null)
                .balance(saved.getBalance())
                .currency(saved.getCurrency() != null ? saved.getCurrency().getCurrencyCode() : null)
                .type(saved.getType() != null ? saved.getType().name() : null)
                .build();
    }
}
