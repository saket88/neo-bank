package com.bank.services;

import com.bank.domain.Transfer;
import com.bank.model.TransferValueObject;

public class TransferService {
    public Transfer transfer(TransferValueObject transferValueObject) {
        return Transfer.builder().build();
    }
}
