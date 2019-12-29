package com.bank.repositories;

import com.bank.domain.Account;

import java.util.List;
import java.util.Optional;

public interface AccountDao {
    Optional<Account> save(Account account);

    Optional<Account> getAccount(Long id);

    Optional<Account> getFromAccountNumber(String accountNo);

    Optional<List<Account>> getAccounts();
}
