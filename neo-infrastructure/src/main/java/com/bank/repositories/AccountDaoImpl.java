package com.bank.repositories;

import com.bank.domain.Account;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;


public class AccountDaoImpl implements AccountDao {

    private final Provider<EntityManagerFactory> entityManagerFactory;

    @Inject
    public AccountDaoImpl(Provider<EntityManagerFactory> entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    };

    @Override
    public Optional<Account> save(Account account) {
        EntityManager entityManager = getEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        Optional<Account> accountSaved= Optional.of(entityManager.merge(account));
        entityTransaction.commit();
        closeEntityManager(entityManager);
        return accountSaved;
    }

    private EntityManager getEntityManager() {
        return entityManagerFactory.get().createEntityManager();
    }

    @Override
    public Optional<Account> getAccount(Long id) {
        EntityManager entityManager = getEntityManager();
        return Optional.of(entityManager.find(Account.class,id));
    }

    @Override
    public Optional<Account> getFromAccountNumber(String accountNo) {
        EntityManager entityManager = getEntityManager();
        List<Account> accounts = entityManager.createQuery("select a from account a where a.accountNumber = :accountNo", Account.class)
                .setParameter("accountNo", accountNo)
                .getResultList();
        closeEntityManager(entityManager);
        Account account = accounts.get(0);
        return Optional.ofNullable(account);
    }

    private void closeEntityManager(EntityManager entityManager) {
        entityManager.close();
    }

    @Override
    public Optional<List<Account>> getAccounts() {
        EntityManager entityManager = getEntityManager();
        List<Account> accounts = entityManager.createQuery("select a  from account a", Account.class)
                .getResultList();

        closeEntityManager(entityManager);
        return Optional.ofNullable(accounts);
    }


}
