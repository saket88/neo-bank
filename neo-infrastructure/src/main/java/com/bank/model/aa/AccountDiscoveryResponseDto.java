package com.bank.model.aa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDiscoveryResponseDto {
    private String ver;
    private LocalDateTime timestamp;
    private String txnid;
    private Customer customer;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Customer {
        private List<DiscoveredAccount> accounts;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiscoveredAccount {
        private String linkRefNumber;
        private String maskedAccNumber;
        private String accType;
        private String accRefNumber;
        private String fiType;
        private String branch;
        private String fiDataFetchStatus;
    }
}
