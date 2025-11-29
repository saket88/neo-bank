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
public class FIRequest {
    private String sessionId;
    private String consentId;
    private String fiuId;
    private SessionStatus sessionStatus;
    private LocalDateTime dataRangeFrom;
    private LocalDateTime dataRangeTo;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private String keyMaterialCryptoAlg;
    private String keyMaterialCurve;
    private String dhPublicKey;
    private String nonce;
}
