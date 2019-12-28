package com.bank.domain;


import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity(name = "transfer")
public class Transfer {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq-gen")
    @SequenceGenerator(name = "seq-gen",
            sequenceName = "transfer_sequence", initialValue = 123)
    private Long id;

    @Column
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(name = "txn_id")
    String transactionId;

    @Column
    String currency;

    @Column
    LocalDateTime createdOn;
}
