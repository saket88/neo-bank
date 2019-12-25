package com.bank.registry;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AccountResource {
    private Gson gson;

    @Inject
    public AccountResource(Gson gson){
        this.gson = gson;
    }

    public String getAccount(Request request, Response response){
        JsonObject requestDto = gson.fromJson(request.body(), JsonObject.class);
        return "{OK}";
    }
}
