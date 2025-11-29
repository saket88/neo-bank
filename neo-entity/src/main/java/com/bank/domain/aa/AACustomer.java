package com.bank.domain.aa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AACustomer {
    private String customerId;
    private String mobile;
    private String name;
    private String email;
    private String pan;
    private LocalDate dateOfBirth;
    private CustomerStatus status;
    private LocalDateTime registeredAt;
    private LocalDateTime lastLoginAt;
}
