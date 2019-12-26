package com.bank.registry;

import com.bank.resource.AccountResource;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import spark.ResponseTransformer;



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
