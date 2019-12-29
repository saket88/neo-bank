package com.bank.api;

import com.bank.domain.exception.InsufficientBalanceException;
import com.bank.model.ErrorMessage;
import com.bank.model.TransferValueObject;
import com.bank.services.TransferService;
import com.bank.services.exception.CurrencyNotAllowedException;
import com.bank.services.exception.InvalidAmountException;
import com.bank.services.exception.NoTransactionAvailableException;
import com.google.gson.Gson;
import com.google.inject.Inject;
import spark.Route;
import spark.Spark;

import javax.management.BadAttributeValueExpException;
import java.math.BigDecimal;
import java.util.NoSuchElementException;

import static spark.Spark.exception;
import static spark.Spark.threadPool;

public class TransferResource {
    private Gson gson;

    private TransferService transferService;
    @Inject
    public TransferResource(Gson gson, TransferService transferService) {
        this.gson = gson;
        this.transferService = transferService;
    }


    public void registerTransferRoutes() {

        Spark.post("/api/v1/transfer", transferMoney());

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

    private Route transferMoney() {
        return (request, response) -> {
            TransferValueObject  transferValueObject= gson.fromJson(request.body(), TransferValueObject.class);
            response.status(201);
            response.type("application-json");
            return gson.toJson(transferService.sendMoney(transferValueObject).get());
        };
    }


}
