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
public class ConsentRequestDto {
    private String ver;
    private LocalDateTime timestamp;
    private String txnid;
    private ConsentDetail consentDetail;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConsentDetail {
        private LocalDateTime consentStart;
        private LocalDateTime consentExpiry;
        private String consentMode;
        private String fetchType;
        private List<String> consentTypes;
        private List<String> fiTypes;
        private DataConsumer dataConsumer;
        private Customer customer;
        private Purpose purpose;
        private FIDataRange fiDataRange;
        private DataLife dataLife;
        private Frequency frequency;
        private List<DataFilter> dataFilter;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataConsumer {
        private String id;
        private String type;
    }

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

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Purpose {
        private String code;
        private String refUri;
        private String text;
    }

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
    public static class DataLife {
        private String unit;
        private Integer value;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Frequency {
        private String unit;
        private Integer value;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataFilter {
        private String type;
        private String operator;
        private String value;
    }
}
