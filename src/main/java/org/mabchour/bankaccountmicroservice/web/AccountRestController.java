package org.mabchour.bankaccountmicroservice.web;

import org.mabchour.bankaccountmicroservice.Repositories.BankAccountRepository;
import org.mabchour.bankaccountmicroservice.dtos.BankAccountDTO;
import org.mabchour.bankaccountmicroservice.entities.BankAccount;
import org.mabchour.bankaccountmicroservice.enums.AccountType;
import org.mabchour.bankaccountmicroservice.mappers.BankAccountMapper;
import org.mabchour.bankaccountmicroservice.service.AccountService;
import org.mabchour.bankaccountmicroservice.dtos.BankAccountRequestDTO;
import org.mabchour.bankaccountmicroservice.dtos.BankAccountResponseDTO;
import org.mabchour.bankaccountmicroservice.entities.AccountSummaryProjection;
import org.mabchour.bankaccountmicroservice.entities.AccountDetailProjection;
import org.mabchour.bankaccountmicroservice.entities.AccountCurrencyProjection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AccountRestController {
    private final BankAccountRepository bankAccountRepository;
    private final AccountService accountService;

    public AccountRestController(BankAccountRepository bankAccountRepository, AccountService accountService) {
        this.bankAccountRepository = bankAccountRepository;
        this.accountService = accountService;
    }

    @GetMapping("/bankAccounts")
    public List<BankAccountResponseDTO> bankAccounts() {
        return bankAccountRepository.findAll().stream()
                .map(BankAccountMapper::toDTO)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/bankAccounts/{id}")
    public ResponseEntity<?> getBankAccount(@PathVariable String id) {
        return bankAccountRepository.findById(id)
                .<ResponseEntity<?>>map(b -> ResponseEntity.ok(toResponse(BankAccountMapper.toDTO(b))))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Account with ID " + id + " not found"));
    }

    @PostMapping("/bankAccounts")
    public ResponseEntity<BankAccountResponseDTO> createBankAccount(@Valid @RequestBody BankAccountRequestDTO dto) {
        if (dto == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            BankAccountResponseDTO created = accountService.addAccount(dto);
            URI location = URI.create(String.format("/bankAccounts/%s", created.getId()));
            return ResponseEntity.created(location).body(created);
        } catch (Exception ex) {
            // journaliser l'erreur selon votre logger (omise ici pour concision)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/bankAccounts/{id}")
    public ResponseEntity<BankAccountDTO> updateBankAccount(@PathVariable String id, @RequestBody(required = false) BankAccountDTO dto) {
        // If body is completely missing, return 400 Bad Request
        if (dto == null) {
            return ResponseEntity.badRequest().build();
        }

        return bankAccountRepository.findById(id)
                .map(existing -> {
                    // merge only non-null fields from incoming dto to existing entity
                    if (dto.getCreatedAt() != null) existing.setCreatedAt(new Date(dto.getCreatedAt()));
                    if (dto.getBalance() != null) existing.setBalance(dto.getBalance());
                    if (dto.getCurrency() != null) existing.setCurrency(java.util.Currency.getInstance(dto.getCurrency()));
                    if (dto.getType() != null) {
                        // dto.getType() is already an AccountType enum, set it directly
                        existing.setType(dto.getType());
                    }
                    BankAccount updated = bankAccountRepository.save(existing);
                    return ResponseEntity.ok(BankAccountMapper.toDTO(updated));
                })
                .orElseGet(() -> {
                    // If account doesn't exist, create it. Ensure id is set.
                    if (dto.getId() == null || dto.getId().trim().isEmpty()) dto.setId(id);
                    BankAccount created = bankAccountRepository.save(BankAccountMapper.toEntity(dto));
                    URI location = URI.create(String.format("/bankAccounts/%s", created.getId()));
                    return ResponseEntity.created(location).body(BankAccountMapper.toDTO(created));
                });
    }

    @PatchMapping("/bankAccounts/{id}")
    public ResponseEntity<BankAccountDTO> patchBankAccount(@PathVariable String id, @RequestBody(required = false) Map<String, Object> updates) {
        if (updates == null || updates.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return bankAccountRepository.findById(id)
                .map(existing -> {
                    // presence of key means the client wants to update the field, even to null
                    if (updates.containsKey("createdAt")) {
                        Object v = updates.get("createdAt");
                        if (v == null) existing.setCreatedAt(null);
                        else if (v instanceof Number) existing.setCreatedAt(new Date(((Number) v).longValue()));
                        else {
                            try {
                                Instant inst = Instant.parse(v.toString());
                                existing.setCreatedAt(Date.from(inst));
                            } catch (Exception ignored) {
                                // ignore parse errors for now
                            }
                        }
                    }
                    if (updates.containsKey("balance")) {
                        Object v = updates.get("balance");
                        if (v == null) existing.setBalance(null);
                        else existing.setBalance(new BigDecimal(v.toString()));
                    }
                    if (updates.containsKey("currency")) {
                        Object v = updates.get("currency");
                        if (v == null) existing.setCurrency(null);
                        else existing.setCurrency(java.util.Currency.getInstance(v.toString()));
                    }
                    if (updates.containsKey("type")) {
                        Object v = updates.get("type");
                        if (v == null) existing.setType(null);
                        else {
                            try {
                                existing.setType(AccountType.valueOf(v.toString()));
                            } catch (Exception ignored) {
                            }
                        }
                    }
                    BankAccount updated = bankAccountRepository.save(existing);
                    return ResponseEntity.ok(BankAccountMapper.toDTO(updated));
                })
                .orElseGet(() -> {
                    // create new account from updates; set id and map known fields
                    BankAccount created = new BankAccount();
                    created.setId(id);
                    if (updates.containsKey("createdAt")) {
                        Object v = updates.get("createdAt");
                        if (v instanceof Number) created.setCreatedAt(new Date(((Number) v).longValue()));
                        else {
                            try {
                                created.setCreatedAt(Date.from(Instant.parse(v.toString())));
                            } catch (Exception ignored) {
                            }
                        }
                    }
                    if (updates.containsKey("balance")) {
                        Object v = updates.get("balance");
                        if (v != null) created.setBalance(new BigDecimal(v.toString()));
                    }
                    if (updates.containsKey("currency")) {
                        Object v = updates.get("currency");
                        if (v != null) created.setCurrency(java.util.Currency.getInstance(v.toString()));
                    }
                    if (updates.containsKey("type")) {
                        Object v = updates.get("type");
                        if (v != null) {
                            try {
                                created.setType(AccountType.valueOf(v.toString()));
                            } catch (Exception ignored) {}
                        }
                    }
                    BankAccount saved = bankAccountRepository.save(created);
                    URI location = URI.create(String.format("/bankAccounts/%s", saved.getId()));
                    return ResponseEntity.created(location).body(BankAccountMapper.toDTO(saved));
                });
    }

    @DeleteMapping("/bankAccounts/{id}")
    public ResponseEntity<Void> deleteBankAccount(@PathVariable String id) {
        if (bankAccountRepository.existsById(id)) {
            bankAccountRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Projections endpoints
    @GetMapping("/bankAccounts/summaries")
    public List<AccountSummaryProjection> summaries() {
        return bankAccountRepository.findAllProjectedBy();
    }

    @GetMapping("/bankAccounts/details")
    public List<AccountDetailProjection> details() {
        return bankAccountRepository.findAllDetailsProjectedBy();
    }

    @GetMapping("/bankAccounts/currencies")
    public List<AccountCurrencyProjection> byCurrency(@RequestParam String currency) {
        return bankAccountRepository.findByCurrency_CurrencyCode(currency);
    }

    // helper to convert internal DTO (used by mapper) to response DTO
    private BankAccountResponseDTO toResponse(BankAccountDTO dto) {
        if (dto == null) return null;
        return BankAccountResponseDTO.builder()
                .id(dto.getId())
                .createdAt(dto.getCreatedAt())
                .balance(dto.getBalance())
                .currency(dto.getCurrency())
                .type(dto.getType() != null ? dto.getType().name() : null)
                .build();
    }

}
