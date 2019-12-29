package com.bank.model;

import com.bank.services.exception.CurrencyNotAllowedException;
import com.bank.services.exception.InvalidAmountException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
public class TransferValueObject {
    String id;
    String fromAccount;
    String toAccount;
    String currency;
    BigDecimal amount;

    public TransferValueObject(String id, String fromAccount, String toAccount, String currency, BigDecimal amount) {
        //Only SEPA transfer is allowed
        if (!currency.equals("EUR"))
            throw new CurrencyNotAllowedException("Only Euro transfer is accepted");
        //Negative or zero Amount can not be transferred

        if (!(amount.doubleValue()>0))
            throw new InvalidAmountException("Only positive amount transfer is accepted");


        this.id = id;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.currency = currency;
        this.amount = amount;
    }
}
