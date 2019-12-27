package com.bank.services;


import com.bank.domain.Account;
import com.bank.model.AccountValueObject;
import com.bank.repositories.AccountDao;

public class AccountService {

    AccountDao accountDao;

    public AccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public AccountValueObject create(AccountValueObject accountValueObject) {
        Account account = accountDao.save(getAccountFrom(accountValueObject));
        return getAccountValueObjectFrom(account);
    }

    private AccountValueObject getAccountValueObjectFrom(Account account) {
        return AccountValueObject.builder()
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .identificationType(account.getIdentificationType())
                .uniqueIdentificationNumber(account.getUniqueIdentificationNumber())
                .name(account.getName())
                .build();
    }

    private Account getAccountFrom(AccountValueObject accountValueObject) {
        return Account.builder()
                .balance(accountValueObject.getBalance())
                .currency(accountValueObject.getCurrency())
                .uniqueIdentificationNumber(accountValueObject.getUniqueIdentificationNumber())
                .identificationType(accountValueObject.getIdentificationType())
                .name(accountValueObject.getName()).build();
    }
}
