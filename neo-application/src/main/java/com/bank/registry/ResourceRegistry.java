package com.bank.registry;



import com.bank.api.AccountResource;
import com.bank.api.TransferResource;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ResourceRegistry {

    private AccountResource accountResource;
    private TransferResource transferResource;

    @Inject
    ResourceRegistry(AccountResource accountResource,TransferResource transferResource){
        this.accountResource=accountResource;
        this.transferResource = transferResource;
    }

    public void registerRoutes(){

        accountResource.registerAccountRoutes();
        transferResource.registerTransferRoutes();


    }
}
