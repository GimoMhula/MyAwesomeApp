package dev.visum.demoapp.model;

import com.google.gson.annotations.SerializedName;

public class AddSalePrestModel {
    @SerializedName("sale_id")
    private int sale_id;

    @SerializedName("value")
    private double value;

    @SerializedName("payment_method_id")
    private int payment_method_id;

    public AddSalePrestModel(int sale_id, double value, int payment_method_id) {
        this.sale_id = sale_id;
        this.value = value;
        this.payment_method_id = payment_method_id;
    }

    public int getSale_id() {
        return sale_id;
    }

    public void setSale_id(int sale_id) {
        this.sale_id = sale_id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getPayment_method_id() {
        return payment_method_id;
    }

    public void setPayment_method_id(int payment_method_id) {
        this.payment_method_id = payment_method_id;
    }
}
