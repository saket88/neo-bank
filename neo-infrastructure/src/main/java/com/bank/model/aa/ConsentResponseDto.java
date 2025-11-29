package com.bank.model.aa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsentResponseDto {
    private String ver;
    private LocalDateTime timestamp;
    private String txnid;
    private CustomerAA customer;
    private String consentHandle;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerAA {
        private String id;
    }
}
