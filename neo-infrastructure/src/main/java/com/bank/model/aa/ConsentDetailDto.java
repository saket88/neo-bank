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
public class ConsentDetailDto {
    private String consentId;
    private String consentHandle;
    private String consentStatus;
    private LocalDateTime createTimestamp;
    private String signedConsent;
    private ConsentUse consentUse;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConsentUse {
        private String logUri;
        private Integer count;
        private LocalDateTime lastUseDateTime;
    }
}
