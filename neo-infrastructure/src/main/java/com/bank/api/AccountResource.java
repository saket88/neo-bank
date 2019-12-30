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
public class AccountResource  {
    private Gson gson;

    private AccountService accountService;
    @Inject
    public AccountResource(Gson gson, AccountService accountService) {
        this.gson = gson;
        this.accountService = accountService;
    }


    public void registerAccountRoutes() {

        Spark.post("/api/v1/account", createAccount());
        Spark.get("/api/v1/account", getAccounts());

        exception(InvalidAmountException.class, (exception, request, response) -> {
            response.status(400);
            response.type("application-json");
            response.body(new Gson().toJson(new ErrorMessage("Bad Request "+exception.getMessage(), 400)));
        });

        exception(CurrencyNotAllowedException.class, (exception, request, response) -> {
            response.status(400);
            response.type("application-json");
            response.body(new Gson().toJson(new ErrorMessage("Bad Request "+exception.getMessage(), 400)));
        });

        exception(NoSuchElementException.class, (exception, request, response) -> {
            response.status(500);
            response.type("application-json");
            response.body(new Gson().toJson(new ErrorMessage("Server Side exception: "+exception.getMessage(), 500)));
        });

        exception(Exception.class, (exception, request, response) -> {
            response.status(500);
            response.type("application-json");
            response.body(new Gson().toJson(new ErrorMessage("A generic exception "+exception.getMessage(), 500)));
        });


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

