package com.roldannanodegre.bakingapp.api;

import android.net.Uri;

public enum RecipesUrl {

    MAY2017("topher/2017/May/59121517_baking/");

    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net";

    private final String jsonPath;

    RecipesUrl(final String size) {
        this.jsonPath = size;
    }

    public String getRecipesUrl() {
        return Uri.parse(BASE_URL).buildUpon().appendEncodedPath(jsonPath).build().toString();
    }

}