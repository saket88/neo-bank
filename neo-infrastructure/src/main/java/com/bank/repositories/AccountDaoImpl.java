package com.bank.repositories;

import com.bank.domain.Account;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;


public class AccountDaoImpl implements AccountDao {
    private final Provider<EntityManager> entityManager;

    @Inject
    public AccountDaoImpl(Provider<EntityManager> entityManager) {
        this.entityManager = entityManager;
    };

    @Override
    public Optional<Account> save(Account account) {
        EntityTransaction entityTransaction = entityManager.get().getTransaction();
        entityTransaction.begin();
        Optional<Account> accountSaved= Optional.of(entityManager.get().merge(account));
        entityTransaction.commit();
        return accountSaved;
    }

    @Override
    public Optional<Account> getAccount(Long id) {
        return Optional.of(entityManager.get().find(Account.class,id));
    }

    @Override
    public Optional<Account> getFromAccountNumber(String accountNo) {

        List<Account> accounts = entityManager.get().createQuery("select a from account a where a.accountNumber = :accountNo", Account.class)
                .setParameter("accountNo", accountNo)
                .getResultList();
        return Optional.ofNullable(accounts.get(0));
    }
}
