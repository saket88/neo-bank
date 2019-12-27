package com.bank.registry;



import com.bank.api.AccountResource;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ResourceRegistry {

    private AccountResource accountResource;

    @Inject
    ResourceRegistry(AccountResource accountResource){
        this.accountResource=accountResource;
    }

    public void registerRoutes(){

        accountResource.registerAccountRoutes();


    }
}
