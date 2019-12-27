package com.bank.repositories;

import com.bank.domain.Account;

import java.util.Optional;


public class AccountDaoImpl implements AccountDao {
    @Override
    public Account save(Account account) {
        return new Account();
    }
}
