package com.roldannanodegre.bakingapp.api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class RecipesRepository {

    private RecipesRepository() { }

    public static BakingApi getBakingApi(RecipesUrl recipesUrl) {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(recipesUrl.getRecipesUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(BakingApi.class);
    }
}
