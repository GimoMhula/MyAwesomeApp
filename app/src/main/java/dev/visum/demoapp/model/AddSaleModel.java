package dev.visum.demoapp.model;

import com.google.gson.annotations.SerializedName;

public class AddSaleModel {
    @SerializedName("agent_id")
    private String agent_id;

    @SerializedName("customer_id")
    private String customer_id;

    @SerializedName("product_id")
    private String product_id;

    @SerializedName("first_prestation")
    private double first_prestation;

    @SerializedName("payment_method_id")
    private String payment_method_id;

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

    public AddSaleModel(String agent_id, String customer_id, String product_id, double first_prestation, String payment_method_id, double lat, double lng, String region, String neighborhood, String city_block, String house_number, String reference_point) {
        this.agent_id = agent_id;
        this.customer_id = customer_id;
        this.product_id = product_id;
        this.first_prestation = first_prestation;
        this.payment_method_id = payment_method_id;
        this.lat = lat;
        this.lng = lng;
        this.region = region;
        this.neighborhood = neighborhood;
        this.city_block = city_block;
        this.house_number = house_number;
        this.reference_point = reference_point;
    }

    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public double getFirst_prestation() {
        return first_prestation;
    }

    public void setFirst_prestation(double first_prestation) {
        this.first_prestation = first_prestation;
    }

    public String getPayment_method_id() {
        return payment_method_id;
    }

    public void setPayment_method_id(String payment_method_id) {
        this.payment_method_id = payment_method_id;
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
}
