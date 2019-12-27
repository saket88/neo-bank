package com.bank.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;


@Entity(name = "account")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Account {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq-gen")
    @SequenceGenerator(name = "seq-gen",
            sequenceName = "user_sequence", initialValue = 1234)
    private Long id;

    @Column
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "unique_id")
    String uniqueIdentificationNumber;

    @Column(name = "type")
    String identificationType;

    @Column
    String name;

    @Column
    String currency;
}
