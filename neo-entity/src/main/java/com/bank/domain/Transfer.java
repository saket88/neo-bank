package com.bank.domain;


import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "transfer")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Transfer {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq-gen")
    @SequenceGenerator(name = "seq-gen",
            sequenceName = "user_sequence", initialValue = 123)
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
