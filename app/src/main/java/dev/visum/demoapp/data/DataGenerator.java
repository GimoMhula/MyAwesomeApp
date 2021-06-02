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



    /**
     * Generate dummy data Item category
     *
     * @param ctx android context
     * @return list of object
     */
    public static List<ItemCategory> getItemCategory(Context ctx) {
        List<ItemCategory> items = new ArrayList<>();
        TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.item_category_icon);
        TypedArray drw_arr_bg = ctx.getResources().obtainTypedArray(R.array.item_category_bg);
        String title_arr[] = ctx.getResources().getStringArray(R.array.item_category_title);
        String brief_arr[] = ctx.getResources().getStringArray(R.array.item_category_brief);
        for (int i = 0; i < title_arr.length; i++) {
            ItemCategory obj = new ItemCategory();
            obj.image = drw_arr.getResourceId(i, -1);
            obj.image_bg = drw_arr_bg.getResourceId(i, -1);
            obj.title = title_arr[i];
            obj.brief = brief_arr[i];
            obj.imageDrw = AppCompatResources.getDrawable(ctx, obj.image);
            items.add(obj);
        }
        return items;
    }



    private static int getRandomIndex(int max) {
        return r.nextInt(max - 1);
    }
}
