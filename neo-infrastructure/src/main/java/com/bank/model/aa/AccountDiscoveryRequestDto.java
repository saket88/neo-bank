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
public class AccountDiscoveryRequestDto {
    private String ver;
    private LocalDateTime timestamp;
    private String txnid;
    private String fipId;
    private Customer customer;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Customer {
        private String id;
        private List<Identifier> identifiers;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Identifier {
        private String type;
        private String value;
    }
}
