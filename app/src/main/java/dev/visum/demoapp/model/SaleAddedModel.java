package dev.visum.demoapp.model;

import com.google.gson.annotations.SerializedName;

public class SaleAddedModel extends SaleCreatedModel {

    @SerializedName("id")
    private int id;

    public SaleAddedModel(String product_id, String customer_id, String user_id, String qty, String payment_method, String date, double lat, double lng, int id) {
        super(product_id, customer_id, user_id, qty, payment_method, date, lat, lng);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
