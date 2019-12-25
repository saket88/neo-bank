package com.bank.module;

import com.bank.NeoApplication;
import com.bank.transformer.GsonTransformer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import spark.ResponseTransformer;

import javax.inject.Singleton;



public class NeoApplicationModule extends AbstractModule {
    @Provides
    public Gson provideGson(){
        return new GsonBuilder().setPrettyPrinting().create();
    }

    @Provides
    public ResponseTransformer provideResponseTransformer(GsonTransformer gsonTransformer){
        return gsonTransformer;
    }

    protected void configure() {
        bind(NeoApplication.class).in(Singleton.class);
    }



}
