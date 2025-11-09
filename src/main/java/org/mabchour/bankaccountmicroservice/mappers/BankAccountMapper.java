package org.mabchour.bankaccountmicroservice.mappers;

import org.mabchour.bankaccountmicroservice.dtos.BankAccountDTO;
import org.mabchour.bankaccountmicroservice.entities.BankAccount;
import org.mabchour.bankaccountmicroservice.enums.AccountType;

import java.util.Currency;
import java.util.Date;
import java.util.UUID;

public class BankAccountMapper {

    public static BankAccount toEntity(BankAccountDTO dto) {
        if (dto == null) return null;
        BankAccount b = new BankAccount();
        // generate id if missing
        if (dto.getId() == null || dto.getId().trim().isEmpty()) {
            b.setId(UUID.randomUUID().toString());
        } else {
            b.setId(dto.getId());
        }
        // createdAt: use provided epoch millis or now if missing
        if (dto.getCreatedAt() != null) b.setCreatedAt(new Date(dto.getCreatedAt()));
        else b.setCreatedAt(new Date());
        b.setBalance(dto.getBalance());
        if (dto.getCurrency() != null) {
            try {
                b.setCurrency(Currency.getInstance(dto.getCurrency()));
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid currency code: " + dto.getCurrency());
            }
        }
        if (dto.getType() != null) {
            try {
                b.setType(AccountType.valueOf(dto.getType()));
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid account type: " + dto.getType());
            }
        }
        return b;
    }

    public static BankAccountDTO toDTO(BankAccount b) {
        if (b == null) return null;
        BankAccountDTO dto = new BankAccountDTO();
        dto.setId(b.getId());
        if (b.getCreatedAt() != null) dto.setCreatedAt(b.getCreatedAt().getTime());
        dto.setBalance(b.getBalance());
        if (b.getCurrency() != null) dto.setCurrency(b.getCurrency().getCurrencyCode());
        if (b.getType() != null) dto.setType(b.getType().name());
        return dto;
    }
}
