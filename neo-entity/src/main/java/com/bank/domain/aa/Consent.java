package com.bank.domain.aa;

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
public class Consent {
    private String consentId;
    private String consentHandle;
    private ConsentStatus status;
    private String customerId;
    private String fiuId;
    private LocalDateTime consentStart;
    private LocalDateTime consentExpiry;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String consentMode;
    private String fetchType;
    private List<String> consentTypes;
    private List<String> fiTypes;
    private String purposeCode;
    private String purposeText;
    private LocalDateTime dataRangeFrom;
    private LocalDateTime dataRangeTo;
    private Integer dataLifeValue;
    private String dataLifeUnit;
    private Integer frequencyValue;
    private String frequencyUnit;
    private String signedConsent;
    private List<String> linkedAccounts;
    private Integer useCount;
    private LocalDateTime lastUsedAt;
}
