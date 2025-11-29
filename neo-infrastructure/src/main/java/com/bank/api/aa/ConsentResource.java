package com.bank.api.aa;

import com.bank.api.BaseResource;
import com.bank.model.aa.ConsentRequestDto;
import com.bank.model.aa.ConsentResponseDto;
import com.bank.model.aa.ConsentDetailDto;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import spark.Route;
import spark.Spark;

import java.time.LocalDateTime;
import java.util.UUID;

@Singleton
public class ConsentResource extends BaseResource {
    private final Gson gson;

    @Inject
    public ConsentResource(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void initialize() {
        super.initialize();
        Spark.post("/api/v1/aa/Consent", createConsent());
        Spark.get("/api/v1/aa/Consent/:consentId", getConsent());
        Spark.put("/api/v1/aa/Consent/:consentId", updateConsent());
        Spark.get("/api/v1/aa/Consent/handle/:consentHandle", getConsentByHandle());
    }

    private Route createConsent() {
        return (request, response) -> {
            ConsentRequestDto consentRequest = gson.fromJson(request.body(), ConsentRequestDto.class);
            
            ConsentResponseDto consentResponse = ConsentResponseDto.builder()
                    .ver("1.0")
                    .timestamp(LocalDateTime.now())
                    .txnid(consentRequest.getTxnid())
                    .customer(ConsentResponseDto.CustomerAA.builder()
                            .id(consentRequest.getConsentDetail().getCustomer().getId())
                            .build())
                    .consentHandle(UUID.randomUUID().toString())
                    .build();

            response.status(201);
            response.type("application/json");
            return gson.toJson(consentResponse);
        };
    }

    private Route getConsent() {
        return (request, response) -> {
            String consentId = request.params(":consentId");
            
            ConsentDetailDto consentDetail = ConsentDetailDto.builder()
                    .consentId(consentId)
                    .consentHandle(UUID.randomUUID().toString())
                    .consentStatus("ACTIVE")
                    .createTimestamp(LocalDateTime.now())
                    .consentUse(ConsentDetailDto.ConsentUse.builder()
                            .count(0)
                            .lastUseDateTime(null)
                            .build())
                    .build();

            response.status(200);
            response.type("application/json");
            return gson.toJson(consentDetail);
        };
    }

    private Route updateConsent() {
        return (request, response) -> {
            String consentId = request.params(":consentId");
            
            ConsentDetailDto consentDetail = ConsentDetailDto.builder()
                    .consentId(consentId)
                    .consentStatus("ACTIVE")
                    .createTimestamp(LocalDateTime.now())
                    .build();

            response.status(200);
            response.type("application/json");
            return gson.toJson(consentDetail);
        };
    }

    private Route getConsentByHandle() {
        return (request, response) -> {
            String consentHandle = request.params(":consentHandle");
            
            ConsentDetailDto consentDetail = ConsentDetailDto.builder()
                    .consentHandle(consentHandle)
                    .consentId(UUID.randomUUID().toString())
                    .consentStatus("PENDING")
                    .createTimestamp(LocalDateTime.now())
                    .build();

            response.status(200);
            response.type("application/json");
            return gson.toJson(consentDetail);
        };
    }
}
