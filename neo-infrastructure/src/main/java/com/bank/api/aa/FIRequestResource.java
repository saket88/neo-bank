package com.bank.api.aa;

import com.bank.api.BaseResource;
import com.bank.model.aa.FIRequestDto;
import com.bank.model.aa.FIRequestResponseDto;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import spark.Route;
import spark.Spark;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Singleton
public class FIRequestResource extends BaseResource {
    private final Gson gson;

    @Inject
    public FIRequestResource(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void initialize() {
        super.initialize();
        Spark.post("/api/v1/aa/FI/request", createFIRequest());
        Spark.get("/api/v1/aa/FI/request/:sessionId", getFIRequestStatus());
    }

    private Route createFIRequest() {
        return (request, response) -> {
            FIRequestDto fiRequest = gson.fromJson(request.body(), FIRequestDto.class);
            
            FIRequestResponseDto fiResponse = FIRequestResponseDto.builder()
                    .ver("1.0")
                    .timestamp(LocalDateTime.now())
                    .txnid(fiRequest.getTxnid())
                    .consentId(fiRequest.getConsent().getId())
                    .sessionId(UUID.randomUUID().toString())
                    .sessionStatus("PENDING")
                    .fiStatusResponse(new ArrayList<>())
                    .build();

            response.status(201);
            response.type("application/json");
            return gson.toJson(fiResponse);
        };
    }

    private Route getFIRequestStatus() {
        return (request, response) -> {
            String sessionId = request.params(":sessionId");
            
            FIRequestResponseDto fiResponse = FIRequestResponseDto.builder()
                    .ver("1.0")
                    .timestamp(LocalDateTime.now())
                    .txnid(UUID.randomUUID().toString())
                    .sessionId(sessionId)
                    .sessionStatus("ACTIVE")
                    .fiStatusResponse(new ArrayList<>())
                    .build();

            response.status(200);
            response.type("application/json");
            return gson.toJson(fiResponse);
        };
    }
}
