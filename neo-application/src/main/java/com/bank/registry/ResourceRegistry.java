package com.bank.registry;

import spark.ResponseTransformer;

import javax.inject.Inject;
import javax.inject.Singleton;

import static spark.Spark.get;

@Singleton
public class ResourceRegistry {

    private ResponseTransformer responseTransformer;
    private AccountResource accountResource;

    @Inject
    ResourceRegistry(AccountResource accountResource,
                     ResponseTransformer responseTransformer){
        this.accountResource=accountResource;
        this.responseTransformer = responseTransformer;
    }

    public void registerRoutes(){

        get("/account/:id", "application/json", accountResource::getAccount, responseTransformer);

    }
}
