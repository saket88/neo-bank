package com.bank.resource;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import spark.Request;
import spark.Response;



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
