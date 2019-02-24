package com.roldannanodegre.bakingapp.custom;

import java.util.Random;

public class Util {

    public static int pickRandomItem(int low, int high){
        return new Random().nextInt((high-1)-low) + low;
    }

}
