package dev.visum.demoapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MySaleKeyModel {
    @SerializedName("sales")
    private ArrayList<ArrayList<Object>> sales;

    public MySaleKeyModel(ArrayList<ArrayList<Object>> sales) {
        this.sales = sales;
    }

    public ArrayList<ArrayList<Object>> getSales() {
        return sales;
    }

    public void setSales(ArrayList<ArrayList<Object>> sales) {
        this.sales = sales;
    }

    @Override
    public String toString() {
        return "MySaleKeyModel{" +
                "sales=" + sales +
                '}';
    }
}
