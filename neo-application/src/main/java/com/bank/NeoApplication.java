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
        configureApplicationPort();
        configureThreads();
        Spark.init();
        awaitInitialization(); // Wait for server to be initialized
        resourceRegistry.registerRoutes();
    }

    private void configureThreads() {
        int maxThreads = 5;
        int minThreads = 2;
        int timeOutMillis = 50000;
        threadPool(maxThreads, minThreads, timeOutMillis);
    }

    private void configureApplicationPort() {
        int port = 9080;
        System.out.println(String.format(" Server is running on  port %s", port));
        port(port);
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
