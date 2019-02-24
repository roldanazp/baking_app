package com.roldannanodegre.bakingapp.api;

import com.roldannanodegre.bakingapp.sqlite.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BakingApi {

    @GET("baking.json")
    Call<List<Recipe>> fetchReceipesMovies();

}
