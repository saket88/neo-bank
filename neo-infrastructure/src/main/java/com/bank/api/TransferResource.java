package com.bank.api;

import com.bank.model.AccountValueObject;
import com.bank.model.TransferValueObject;
import com.bank.services.AccountService;
import com.bank.services.TransferService;
import com.google.gson.Gson;
import com.google.inject.Inject;
import spark.Route;
import spark.Spark;

public class TransferResource {
    private Gson gson;

    private TransferService transferService;
    @Inject
    public TransferResource(Gson gson, TransferService transferService) {
        this.gson = gson;
        this.transferService = transferService;
    }


    public void registerAccountRoutes() {

        Spark.post("/api/v1/transfer", transferMoney());
        Spark.exception(Exception.class, (exception, request, response) -> {
            exception.printStackTrace();
        });
    }

    private Route transferMoney() {
        return (request, response) -> {
            TransferValueObject  transferValueObject= gson.fromJson(request.body(), TransferValueObject.class);
            response.status(201);
            response.type("application-json");
            return gson.toJson(TransferValueObject
                    .builder()
                    .id("txn1-123")
            .build());
        };
    }
}
