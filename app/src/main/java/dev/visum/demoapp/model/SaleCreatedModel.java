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

    @SerializedName("region")
    private String region;

    @SerializedName("neighborhood")
    private String neighborhood;

    @SerializedName("city_block")
    private String city_block;

    @SerializedName("house_number")
    private String house_number;

    @SerializedName("reference_point")
    private String reference_point;

    @SerializedName("totalPrice")
    private String totalPrice;
    


    public SaleCreatedModel(String product_id, String customer_id, String user_id, String qty, String payment_method, String date, double lat, double lng, String region, String neighborhood, String city_block, String house_number, String reference_point, String totalPrice) {
        this.product_id = product_id;
        this.customer_id = customer_id;
        this.user_id = user_id;
        this.qty = qty;
        this.payment_method = payment_method;
        this.date = date;
        this.lat = lat;
        this.lng = lng;
        this.region = region;
        this.neighborhood = neighborhood;
        this.city_block = city_block;
        this.house_number = house_number;
        this.reference_point = reference_point;
        this.totalPrice = totalPrice;

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

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCity_block() {
        return city_block;
    }

    public void setCity_block(String city_block) {
        this.city_block = city_block;
    }

    public String getHouse_number() {
        return house_number;
    }

    public void setHouse_number(String house_number) {
        this.house_number = house_number;
    }

    public String getReference_point() {
        return reference_point;
    }

    public void setReference_point(String reference_point) {
        this.reference_point = reference_point;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }



    @Override
    public String toString() {
        return "SaleCreatedModel{" +
                "product_id='" + product_id + '\'' +
                ", customer_id='" + customer_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", qty='" + qty + '\'' +
                ", payment_method='" + payment_method + '\'' +
                ", date='" + date + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", region='" + region + '\'' +
                ", neighborhood='" + neighborhood + '\'' +
                ", city_block='" + city_block + '\'' +
                ", house_number='" + house_number + '\'' +
                ", reference_point='" + reference_point + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                '}';
    }
}
