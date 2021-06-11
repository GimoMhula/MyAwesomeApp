package dev.visum.demoapp.data;

import android.content.Context;
import android.content.res.TypedArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.appcompat.content.res.AppCompatResources;
import dev.visum.demoapp.R;
import dev.visum.demoapp.model.ItemCategory;

@SuppressWarnings("ResourceType")
public class DataGenerator {

    private static Random r = new Random();

    public static int randInt(int max) {
        int min = 0;
        return r.nextInt((max - min) + 1) + min;
    }


    private static int getRandomIndex(int max) {
        return r.nextInt(max - 1);
    }
}
