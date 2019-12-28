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
        //Only SEPA transfer is allowed
        if (!currency.equals("EUR"))
            throw new CurrencyNotAllowedException("Only Euro transfer is accepted");
        //Negative or zero Amount can not be transferred

        if (!(balance.doubleValue()>0))
            throw new InvalidAmountException("Only positive amount transfer is accepted");
        this.uniqueIdentificationNumber = uniqueIdentificationNumber;
        this.identificationType = identificationType;
        this.name = name;
        this.balance = balance;
        this.currency = currency;
        this.accountNumber = accountNumber;
    }
}
