package com.bank.transformer;

import com.google.gson.Gson;
import spark.ResponseTransformer;

import javax.inject.Inject;

public class GsonTransformer implements ResponseTransformer {

    private final Gson gson;

    @Inject
    GsonTransformer(Gson gson){
        this.gson = gson;
    }
    public String render(Object object) throws Exception {
        return gson.toJson(object);
    }
}
