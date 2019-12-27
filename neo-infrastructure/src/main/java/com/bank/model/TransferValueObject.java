package com.bank.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferValueObject {
    String id;
    String fromAccount;
    String toAccount;
    String currency;
    BigDecimal amount;
}
