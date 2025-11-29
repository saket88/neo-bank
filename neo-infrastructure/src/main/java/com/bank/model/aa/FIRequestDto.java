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
public class FIRequestDto {
    private String ver;
    private LocalDateTime timestamp;
    private String txnid;
    private FIDataRange fiDataRange;
    private Consent consent;
    private KeyMaterial keyMaterial;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FIDataRange {
        private LocalDateTime from;
        private LocalDateTime to;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Consent {
        private String id;
        private String digitalSignature;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeyMaterial {
        private String cryptoAlg;
        private String curve;
        private String params;
        private DHPublicKey dhPublicKey;
        private String nonce;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DHPublicKey {
        private LocalDateTime expiry;
        private String parameters;
        private String keyValue;
    }
}
