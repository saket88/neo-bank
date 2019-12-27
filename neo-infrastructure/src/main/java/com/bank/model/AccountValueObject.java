package com.bank.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountValueObject {
    String uniqueIdentificationNumber;
    String identificationType;
    String name;
    BigDecimal balance;
    String currency;
}
