package org.mabchour.bankaccountmicroservice;

import org.mabchour.bankaccountmicroservice.Repositories.BankAccountRepository;
import org.mabchour.bankaccountmicroservice.entities.BankAccount;
import org.mabchour.bankaccountmicroservice.enums.AccountType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;
import java.util.UUID;

@SpringBootApplication
public class BankAccountMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankAccountMicroserviceApplication.class, args);

    }
    @Bean
    CommandLineRunner start(BankAccountRepository bankAccountRepository){
        return args -> {
            for (int i = 1; i <= 10; i++) {
                BankAccount bankAccount = BankAccount.builder().id(UUID.randomUUID().toString()).type(Math.random()>0.5? AccountType.CURRENT_ACCOUNT:AccountType.SAVING_ACCOUNT).balance(BigDecimal.valueOf(10000+Math.random()*90000)).currency(Currency.getInstance("MAD")).createdAt(new Date()).build();
           bankAccountRepository.save(bankAccount);
            }

        };
    };
}
