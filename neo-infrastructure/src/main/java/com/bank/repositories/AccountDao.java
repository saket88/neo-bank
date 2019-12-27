package com.bank.repositories;

import com.bank.domain.Account;

public interface AccountDao {
    Account save(Account account);

    Account getAccount(Long id);
}
