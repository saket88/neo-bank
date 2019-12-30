package com.bank.repositories;

import com.bank.domain.Transfer;
import com.bank.services.exception.NoTransactionAvailableException;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.Optional;

public class TransferDaoImpl implements TransferDao {


    private final Provider<EntityManagerFactory> entityManagerFactory;

    @Inject
    public TransferDaoImpl(Provider<EntityManagerFactory> entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    };

    @Override
    public Optional<Transfer> save(Transfer transfer) {
        EntityManager entityManager=getEntityManager();
        EntityTransaction entityTransaction =entityManager.getTransaction();
        entityTransaction.begin();
        Optional  transferSaved = Optional.of(entityManager.merge(transfer));
        entityTransaction.commit();
        entityManager.close();
        return transferSaved;
    }

    @Override
    public Optional<Transfer> getTransfer(Long id) {
        EntityManager entityManager=getEntityManager();
        return Optional.ofNullable(Optional.ofNullable(entityManager.find(Transfer.class, id))
                .orElseThrow(() -> new NoTransactionAvailableException("This transaction is not available")));
    }

    private EntityManager getEntityManager() {
        return entityManagerFactory.get().createEntityManager();
    }
}
