package com.bank.model;

import com.bank.services.exception.CurrencyNotAllowedException;
import com.bank.services.exception.InvalidAmountException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Builder
public class AccountValueObject {
    String uniqueIdentificationNumber;
    String identificationType;
    String name;
    BigDecimal balance;
    String currency;
    String accountNumber;

    public AccountValueObject(String uniqueIdentificationNumber, String identificationType, String name, BigDecimal balance, String currency, String accountNumber) {

        this.uniqueIdentificationNumber = uniqueIdentificationNumber;
        this.identificationType = identificationType;
        this.name = name;
        this.balance = balance;
        this.currency = currency;
        this.accountNumber = accountNumber;
    }
}
