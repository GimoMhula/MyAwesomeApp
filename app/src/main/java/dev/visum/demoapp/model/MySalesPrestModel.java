package dev.visum.demoapp.model;

import com.google.gson.annotations.SerializedName;

public class MySalesPrestModel {

    @SerializedName("id")
    private int id;

    @SerializedName("value")
    private double value;

    @SerializedName("date")
    private String date;

    @SerializedName("payment_method")
    private String payment_method;

    public MySalesPrestModel(int id, double value, String date, String payment_method) {
        this.id = id;
        this.value = value;
        this.date = date;
        this.payment_method = payment_method;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
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
}
