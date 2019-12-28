package com.bank.services;


import com.bank.domain.Account;
import com.bank.model.AccountValueObject;
import com.bank.repositories.AccountDao;
import com.bank.services.exception.CurrencyNotAllowedException;
import com.bank.services.exception.InvalidAmountException;
import com.google.inject.Inject;
import com.google.inject.Singleton;
@Singleton
public class AccountService {

    AccountDao accountDao;

    @Inject
    public AccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public AccountValueObject create(AccountValueObject accountValueObject) {
        //Only SEPA transfer is allowed
        if (!accountValueObject.getCurrency().equals("EUR"))
            throw new CurrencyNotAllowedException("Only Euro transfer is accepted");
        //Negative or zero Amount can not be transferred

        if (!(accountValueObject.getBalance().doubleValue()>0))
            throw new InvalidAmountException("Only positive amount transfer is accepted");

        Account account = accountDao.save(getAccountFrom(accountValueObject)).get();
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
                .accountNumber("NL"+Math.abs(100*Math.random()))
                .currency(accountValueObject.getCurrency())
                .uniqueIdentificationNumber(accountValueObject.getUniqueIdentificationNumber())
                .identificationType(accountValueObject.getIdentificationType())
                .name(accountValueObject.getName()).build();
    }
}
