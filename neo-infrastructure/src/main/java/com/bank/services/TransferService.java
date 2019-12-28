package com.bank.services;

import com.bank.domain.Account;
import com.bank.domain.Transfer;
import com.bank.model.TransferValueObject;
import com.bank.repositories.AccountDao;
import com.bank.repositories.TransferDao;
import com.google.inject.Inject;
import com.google.inject.Singleton;

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

    public Optional<Transfer> sendMoney(TransferValueObject transferValueObject) {

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
}
