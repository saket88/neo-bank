package com.bank.api;

import com.bank.model.TransferValueObject;
import com.bank.services.TransferService;
import com.google.gson.Gson;
import com.google.inject.Inject;
import spark.Route;
import spark.Spark;

public class TransferResource extends BaseResource{
    private Gson gson;

    private TransferService transferService;
    @Inject
    public TransferResource(Gson gson, TransferService transferService) {
        this.gson = gson;
        this.transferService = transferService;
    }

    @Override
    public void initialize(){
        super.initialize();
        Spark.post("/api/v1/transfer", transferMoney());

    }

    private Route transferMoney() {
        return (request, response) -> {
            TransferValueObject  transferValueObject= gson.fromJson(request.body(), TransferValueObject.class);
            response.status(201);
            response.type("application-json");
            return gson.toJson(transferService.sendMoney(transferValueObject).get());
        };
    }


}
