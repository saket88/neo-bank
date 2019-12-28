package com.bank.repositories;

import com.bank.domain.Transfer;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.persistence.EntityManager;
import java.util.Optional;

public class TransferDaoImpl implements TransferDao {


    private final Provider<EntityManager> entityManager;

    @Inject
    public TransferDaoImpl(Provider<EntityManager> entityManager) {
        this.entityManager = entityManager;
    };

    @Override
    public Optional<Transfer> save(Transfer transfer) {
        return Optional.of(entityManager.get().merge(transfer));
    }

    @Override
    public Optional<Transfer> getTransfer(Long id) {
        return Optional.of(entityManager.get().find(Transfer.class,id));
    }
}