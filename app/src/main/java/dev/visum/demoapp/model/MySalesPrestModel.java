package dev.visum.demoapp.model;

import com.google.gson.annotations.SerializedName;

public class MySalesPrestModel {

    @SerializedName("id")
    private String id;

    @SerializedName("sale_id")
    private String sale_id;

    @SerializedName("value")
    private double value;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("payment_method_id")
    private String payment_method_id;

    public MySalesPrestModel(String id, String sale_id, double value, String created_at, String payment_method_id) {
        this.id = id;
        this.sale_id = sale_id;
        this.value = value;
        this.created_at = created_at;
        this.payment_method_id = payment_method_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSale_id() {
        return sale_id;
    }

    public void setSale_id(String sale_id) {
        this.sale_id = sale_id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getPayment_method_id() {
        return payment_method_id;
    }

    public void setPayment_method_id(String payment_method_id) {
        this.payment_method_id = payment_method_id;
    }
}
