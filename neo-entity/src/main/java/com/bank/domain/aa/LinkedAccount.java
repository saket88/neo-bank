package com.bank.domain.aa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkedAccount {
    private String linkRefNumber;
    private String customerId;
    private String fipId;
    private String fipName;
    private String maskedAccNumber;
    private String accType;
    private String fiType;
    private String branch;
    private AccountLinkStatus status;
    private LocalDateTime linkedDate;
    private LocalDateTime lastAccessedDate;
}
