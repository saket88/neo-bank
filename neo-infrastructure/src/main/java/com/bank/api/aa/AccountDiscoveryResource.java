package com.bank.api.aa;

import com.bank.api.BaseResource;
import com.bank.model.aa.AccountDiscoveryRequestDto;
import com.bank.model.aa.AccountDiscoveryResponseDto;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import spark.Route;
import spark.Spark;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Singleton
public class AccountDiscoveryResource extends BaseResource {
    private final Gson gson;

    @Inject
    public AccountDiscoveryResource(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void initialize() {
        super.initialize();
        Spark.post("/api/v1/aa/Accounts/discover", discoverAccounts());
        Spark.post("/api/v1/aa/Accounts/link", linkAccounts());
        Spark.get("/api/v1/aa/Accounts", getLinkedAccounts());
    }

    private Route discoverAccounts() {
        return (request, response) -> {
            AccountDiscoveryRequestDto discoveryRequest = gson.fromJson(request.body(), AccountDiscoveryRequestDto.class);
            
            List<AccountDiscoveryResponseDto.DiscoveredAccount> accounts = new ArrayList<>();
            accounts.add(AccountDiscoveryResponseDto.DiscoveredAccount.builder()
                    .linkRefNumber(UUID.randomUUID().toString())
                    .maskedAccNumber("XXXXXX1234")
                    .accType("SAVINGS")
                    .fiType("DEPOSIT")
                    .branch("Main Branch")
                    .fiDataFetchStatus("AVAILABLE")
                    .build());

            AccountDiscoveryResponseDto discoveryResponse = AccountDiscoveryResponseDto.builder()
                    .ver("1.0")
                    .timestamp(LocalDateTime.now())
                    .txnid(discoveryRequest.getTxnid())
                    .customer(AccountDiscoveryResponseDto.Customer.builder()
                            .accounts(accounts)
                            .build())
                    .build();

            response.status(200);
            response.type("application/json");
            return gson.toJson(discoveryResponse);
        };
    }

    private Route linkAccounts() {
        return (request, response) -> {
            response.status(200);
            response.type("application/json");
            return gson.toJson("{\"status\":\"success\",\"message\":\"Accounts linked successfully\"}");
        };
    }

    private Route getLinkedAccounts() {
        return (request, response) -> {
            String customerId = request.queryParams("customerId");
            
            response.status(200);
            response.type("application/json");
            return gson.toJson("{\"customerId\":\"" + customerId + "\",\"accounts\":[]}");
        };
    }
}
