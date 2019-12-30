package com.bank.api;

import com.bank.model.AccountValueObject;
import com.bank.model.ErrorMessage;
import com.bank.services.AccountService;
import com.bank.services.exception.CurrencyNotAllowedException;
import com.bank.services.exception.InvalidAmountException;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import spark.Route;
import spark.Spark;

import java.util.NoSuchElementException;

import static spark.Spark.exception;

@Singleton
public class AccountResource  extends BaseResource{
    private Gson gson;

    private AccountService accountService;
    @Inject
    public AccountResource(Gson gson, AccountService accountService) {
        this.gson = gson;
        this.accountService = accountService;
    }


    @Override
    public void initialize() {

        super.initialize();
        Spark.post("/api/v1/account", createAccount());
        Spark.get("/api/v1/account", getAccounts());


    }


    private Route createAccount() {
        return (request, response) -> {
            AccountValueObject accountValueObject = gson.fromJson(request.body(), AccountValueObject.class);
            response.status(201);
            response.type("application-json");
            return gson.toJson(accountService.create(accountValueObject));
        };
    }

    private Route getAccounts() {
        return (request, response) -> {
            response.status(200);
            response.type("application-json");
            return gson.toJson(accountService.getAccounts());
        };
    }




}

