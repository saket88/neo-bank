package com.bank;

import com.bank.module.NeoApplicationModule;
import com.bank.registry.ResourceRegistry;
import com.google.inject.Guice;
import com.google.inject.Inject;
import spark.Spark;

import static spark.Spark.*;

public class NeoApplication {
    ResourceRegistry resourceRegistry;

    @Inject
    public NeoApplication(final  ResourceRegistry resourceRegistry) {
        this.resourceRegistry = resourceRegistry;
    }

    private void start(){

        stop();
        // Configure Spark
        int port = 9080;
        if (System.getenv("PORT") != null){
            port = Integer.parseInt(System.getenv("PORT"));
        }
        System.out.println(String.format("Starting server on port %s", port));
        port(port);
        int maxThreads = 5;
        int minThreads = 2;
        int timeOutMillis = 50000;
        threadPool(maxThreads, minThreads, timeOutMillis);
        Spark.init();
        awaitInitialization(); // Wait for server to be initialized
        resourceRegistry.registerRoutes();
    }


    public void stop(){
        Spark.stop();
        awaitStop();
    }



    public static void main(String[] args) {
        Guice.createInjector(
                new NeoApplicationModule())
                .getInstance(NeoApplication.class)
                .start();
    }
}
