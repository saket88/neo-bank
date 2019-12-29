package com.bank.services;


import com.bank.domain.Account;
import com.bank.model.AccountValueObject;
import com.bank.repositories.AccountDao;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class AccountService {

    AccountDao accountDao;

    @Inject
    public AccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public AccountValueObject create(AccountValueObject accountValueObject) {

        Account account = accountDao.save(getAccountFrom(accountValueObject)).get();
        return getAccountValueObjectFrom(account);
    }

    private AccountValueObject getAccountValueObjectFrom(Account account) {
        return AccountValueObject.builder()
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .identificationType(account.getIdentificationType())
                .accountNumber(account.getAccountNumber())
                .uniqueIdentificationNumber(account.getUniqueIdentificationNumber())
                .name(account.getName())
                .build();
    }

    private Account getAccountFrom(AccountValueObject accountValueObject) {
        return Account.builder()
                .balance(accountValueObject.getBalance())
                .accountNumber("NL"+Math.round(10000*Math.random()))
                .currency(accountValueObject.getCurrency())
                .uniqueIdentificationNumber(accountValueObject.getUniqueIdentificationNumber())
                .identificationType(accountValueObject.getIdentificationType())
                .name(accountValueObject.getName()).build();
    }

    public List<AccountValueObject> getAccounts() {
        List<Account> accounts = accountDao.getAccounts().get();
        return accounts.stream()
                .map(this::getAccountValueObjectFrom)
                .collect(Collectors.toList());
    }

}
