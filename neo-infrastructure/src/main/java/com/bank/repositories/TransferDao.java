package com.bank.repositories;

import com.bank.domain.Transfer;

import java.util.Optional;

public interface TransferDao {

    Optional<Transfer> save(Transfer transfer);

    Optional<Transfer> getTransfer(Long id);
}
