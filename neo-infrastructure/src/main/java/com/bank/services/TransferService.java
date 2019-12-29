package com.bank.services;

import com.bank.domain.Account;
import com.bank.domain.Transfer;
import com.bank.model.TransferValueObject;
import com.bank.repositories.AccountDao;
import com.bank.repositories.TransferDao;
import com.bank.services.exception.CurrencyNotAllowedException;
import com.bank.services.exception.InvalidAmountException;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.management.BadAttributeValueExpException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Singleton
public class TransferService {

    private AccountDao accountDao;

    private TransferDao transferDao;


    @Inject
    public TransferService(AccountDao accountDao, TransferDao transferDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
    }

    public Optional<Transfer> sendMoney(TransferValueObject transferValueObject) throws BadAttributeValueExpException {

        validateTransferValueObject(transferValueObject);

        Account fromAccount = accountDao.getFromAccountNumber(transferValueObject.getFromAccount()).get();
        Account toAccount = accountDao.getFromAccountNumber(transferValueObject.getToAccount()).get();

        fromAccount.withdraw(transferValueObject.getAmount());
        toAccount.deposit(transferValueObject.getAmount());

        accountDao.save(fromAccount);
        accountDao.save(toAccount);
        return transferDao.save(Transfer.builder()
                .amount(transferValueObject.getAmount())
                .createdOn(LocalDateTime.now())
                .currency(transferValueObject.getCurrency())
                .transactionId("TXN-"+Math.round(100*Math.random()))
                .build());
    }
    private void validateTransferValueObject(TransferValueObject transferValueObject) throws BadAttributeValueExpException {
        String fromAccount = transferValueObject.getFromAccount();
        String toAccount = transferValueObject.getToAccount();
        String currency = transferValueObject.getCurrency();
        BigDecimal amount = transferValueObject.getAmount();

        if (!currency.equals("EUR"))
            throw new CurrencyNotAllowedException("Only Euro transfer is accepted");
        //Negative or zero Amount can not be transferred

        if (!(amount.doubleValue()>0))
            throw new InvalidAmountException("Only positive amount transfer is accepted");

        if (fromAccount==null || toAccount==null || fromAccount.isEmpty()|| toAccount.isEmpty())
            throw new BadAttributeValueExpException("Accounts should not be empty");
    }
}
