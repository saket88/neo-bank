package com.bank.services;


import com.bank.domain.Account;
import com.bank.model.AccountValueObject;
import com.bank.repositories.AccountDao;
import com.bank.services.exception.CurrencyNotAllowedException;
import com.bank.services.exception.InvalidAmountException;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.management.BadAttributeValueExpException;
import java.math.BigDecimal;
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

    public AccountValueObject create(AccountValueObject accountValueObject) throws BadAttributeValueExpException {

        validateAccountvalueObject(accountValueObject);
        Account account = accountDao.save(getAccountFrom(accountValueObject)).get();
        return getAccountValueObjectFrom(account);
    }

    private void validateAccountvalueObject(AccountValueObject accountValueObject) throws BadAttributeValueExpException {
        String currency =accountValueObject.getCurrency();
        BigDecimal balance = accountValueObject.getBalance();
        String name = accountValueObject.getName();
        String identificaitonType = accountValueObject.getIdentificationType();
        String uniqueId = accountValueObject.getUniqueIdentificationNumber();

        //Only SEPA transfer is allowed
        if (!currency.equals("EUR"))
            throw new CurrencyNotAllowedException("Only Euro transfer is accepted");
        //Negative or zero Amount can not be transferred

        if (!(balance.doubleValue()>0))
            throw new InvalidAmountException("Only positive amount transfer is accepted");
        if (isNullOrEmpty(name)|| isNullOrEmpty(uniqueId)|| isNullOrEmpty(currency)|| isNullOrEmpty(identificaitonType)|| isNullOrEmpty(balance.toPlainString()))
            throw new BadAttributeValueExpException(" The value cannot be null or empty");
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

    public boolean isNullOrEmpty(String value){
        return (value==null || value.isBlank());
    }

}
