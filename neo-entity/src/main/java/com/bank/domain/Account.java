package com.bank.domain;

import com.bank.domain.exception.InsufficientBalanceException;
import lombok.*;

import javax.naming.InsufficientResourcesException;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


@Entity(name = "account")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Account {

    @Transient
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

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

    @Column(name = "account_no")
    String accountNumber;

    @Column(name = "type")
    String identificationType;

    @Column
    String name;

    @Column
    String currency;

    public void withdraw(BigDecimal amount) {
        this.readWriteLock.writeLock().lock();
        if (!(this.balance.subtract(amount).doubleValue()>0.0))
            throw new InsufficientBalanceException("You need some serious balance");
        try {
            this.balance = this.balance.subtract(amount);
        }finally {
            this.readWriteLock.writeLock().unlock();
        }

    }

    public void deposit(BigDecimal amount) {

        this.readWriteLock.writeLock().lock();
        try {
        this.balance = this.balance.add(amount);
        }finally {
            this.readWriteLock.writeLock().unlock();
        }

    }
}
