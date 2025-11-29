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
public class FIRequestResponseDto {
    private String ver;
    private LocalDateTime timestamp;
    private String txnid;
    private String consentId;
    private String sessionId;
    private String sessionStatus;
    private List<FIStatus> fiStatusResponse;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FIStatus {
        private String fipID;
        private List<AccountFIStatus> accounts;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountFIStatus {
        private String linkRefNumber;
        private String fiStatus;
        private String description;
    }
}
