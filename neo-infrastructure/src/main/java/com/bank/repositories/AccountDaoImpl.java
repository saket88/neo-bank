package com.bank.repositories;

import com.bank.domain.Account;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.persistence.EntityManager;
import java.util.Optional;


public class AccountDaoImpl implements AccountDao {
    private final Provider<EntityManager> entityManager;

    @Inject
    public AccountDaoImpl(Provider<EntityManager> entityManager) {
        this.entityManager = entityManager;
    };

    @Override
    public Account save(Account account) {
        return entityManager.get().merge(account);
    }

    @Override
    public Account getAccount(Long id) {
        return entityManager.get().find(Account.class,id);
    }
}
