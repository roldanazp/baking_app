package com.roldannanodegre.bakingapp.sqlite;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@Entity(tableName = "ingredients", primaryKeys = {"receipId","ingredient"})
public class Ingredient {

    @ColumnInfo
    @NonNull
    private Long receipId;

    @ColumnInfo
    private Double quantity;

    @ColumnInfo
    private String measure;

    @ColumnInfo
    @NonNull
    private String ingredient;

    @ColumnInfo
    private boolean acquired;

    @NonNull
    public Long getReceipId() {
        return receipId;
    }

    public void setReceipId(@NonNull Long receipId) {
        this.receipId = receipId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    @NonNull
    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(@NonNull String ingredient) {
        this.ingredient = ingredient;
    }

    public boolean isAcquired() {
        return acquired;
    }

    public void setAcquired(boolean acquired) {
        this.acquired = acquired;
    }

    @Override
    public boolean equals(@Nullable Object obj) {

        if (obj instanceof Ingredient) {
            Ingredient ingredient = (Ingredient) obj;
            return (receipId == ingredient.receipId) &&
                    (this.ingredient.equals(ingredient.ingredient));
        } else {
            return false;
        }

    }
}
