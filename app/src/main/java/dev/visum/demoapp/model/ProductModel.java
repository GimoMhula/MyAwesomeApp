package dev.visum.demoapp.model;

import android.graphics.drawable.Drawable;

public class ProductModel {

    public int image;
    public Drawable imageDrw;
    public String title;
    public String category;
    public String price;
    public String url;

    public ProductModel(int image, Drawable imageDrw, String title, String price, String url) {
        this.image = image;
        this.imageDrw = imageDrw;
        this.title = title;
        this.price = price;
        this.url = url;
    }

    public ProductModel(String title, String category, String price, String url) {
        this.image = 0;
        this.title = title;
        this.category = category;
        this.price = price;
        this.url = url;
        this.imageDrw = null;
    }
}
