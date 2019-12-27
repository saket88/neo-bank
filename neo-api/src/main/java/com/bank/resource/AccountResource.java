package com.bank.resource;

import com.bank.model.AccountValueObject;;
import com.bank.services.AccountService;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import spark.Request;
import spark.Response;

import static spark.Spark.post;


@Singleton
public class AccountResource {
    private AccountService accountService;
    private Gson gson;
    private static  final  String APPLICATION_JSON = "application/json";;

    @Inject
    public AccountResource(Gson gson, AccountService accountService){
        this.accountService = accountService;
        this.gson = gson;
    }

    public String postAccount(Request request, Response response){
        AccountValueObject accountValueObject = gson.fromJson(request.body(), AccountValueObject.class);
        response.type("application/json");
        response.status(201);
        return new Gson().toJson(accountService.create(accountValueObject));
    }

    public void registerAccountRoutes() {
        post("/api/v1/account", APPLICATION_JSON, this::postAccount);
    }
}
