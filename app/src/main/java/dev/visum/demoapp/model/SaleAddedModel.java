package dev.visum.demoapp.model;

import com.google.gson.annotations.SerializedName;

public class SaleAddedModel extends SaleCreatedModel {

    @SerializedName("id")
    private int id;

    public SaleAddedModel(String product_id, String customer_id, String user_id, String qty, String payment_method, String date, double lat, double lng, String region, String neighborhood, String city_block, String house_number, String reference_point, int id, String totalPrice) {
        super(product_id, customer_id, user_id, qty, payment_method, date, lat, lng, region, neighborhood, city_block, house_number, reference_point,totalPrice);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
