package dev.visum.demoapp.model;

import com.google.gson.annotations.SerializedName;

public class SaleCreatedModel {

    @SerializedName("product_id")
    private String product_id;

    @SerializedName("customer_id")
    private String customer_id;

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("qty")
    private String qty;

    @SerializedName("payment_method")
    private String payment_method;

    @SerializedName("date")
    private String date;

    @SerializedName("lat")
    private double lat;

    @SerializedName("lng")
    private double lng;

    public SaleCreatedModel(String product_id, String customer_id, String user_id, String qty, String payment_method, String date, double lat, double lng) {
        this.product_id = product_id;
        this.customer_id = customer_id;
        this.user_id = user_id;
        this.qty = qty;
        this.payment_method = payment_method;
        this.date = date;
        this.lat = lat;
        this.lng = lng;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "?" +
                "product_id=" + product_id  +
                "&customer_id=" + customer_id  +
                "&user_id=" + user_id  +
                "&qty=" + qty  +
                "&payment_method=" + payment_method  +
                "&date=" + date  +
                "&lat=" + lat +
                "&lng=" + lng;
    }
}
