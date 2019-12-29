package com.bank.api;

import com.bank.domain.exception.InsufficientBalanceException;
import com.bank.model.ErrorMessage;
import com.bank.services.exception.CurrencyNotAllowedException;
import com.bank.services.exception.InvalidAmountException;
import com.bank.services.exception.NoTransactionAvailableException;
import com.google.gson.Gson;
import spark.Spark;

import java.util.NoSuchElementException;

import static spark.Spark.exception;

public abstract class BaseResource {

    public void registerRoutes() {

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

        exception(NoTransactionAvailableException.class, (exception, request, response) -> {
            response.status(500);
            response.type("application-json");
            response.body(new Gson().toJson(new ErrorMessage("Bad Request "+exception.getMessage(), 500)));
        });

        exception(InsufficientBalanceException.class, (exception, request, response) -> {
            response.status(500);
            response.type("application-json");
            response.body(new Gson().toJson(new ErrorMessage("Server Side exception: "+exception.getMessage(), 500)));
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
}
