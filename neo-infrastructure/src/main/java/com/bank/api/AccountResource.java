package com.bank.api;

import com.bank.model.AccountValueObject;
import com.bank.services.AccountService;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import spark.Route;
import spark.Spark;

@Singleton
public class AccountResource {
    private Gson gson;

    private AccountService accountService;
    @Inject
    public AccountResource(Gson gson, AccountService accountService) {
        this.gson = gson;
        this.accountService = accountService;
    }


    public void registerAccountRoutes() {

        Spark.post("/api/v1/account", createAccount());
    }

    private Route createAccount() {
        return (request, response) -> {
            AccountValueObject accountValueObject = gson.fromJson(request.body(), AccountValueObject.class);
            response.status(201);
            response.type("application-json");
            return gson.toJson(accountService.create(accountValueObject));
        };
    }


}

