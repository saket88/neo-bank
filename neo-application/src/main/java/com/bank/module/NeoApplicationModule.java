package com.bank.module;

import com.bank.NeoApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.inject.Singleton;



public class NeoApplicationModule extends AbstractModule {
    @Provides
    public Gson provideGson(){
        return new GsonBuilder().setPrettyPrinting().create();
    }


    protected void configure() {
        bind(NeoApplication.class).in(Singleton.class);
    }



}
