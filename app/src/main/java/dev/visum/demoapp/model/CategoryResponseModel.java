package dev.visum.demoapp.model;

import com.google.gson.annotations.SerializedName;

public class CategoryResponseModel {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("prestations_num")
    private String prestations_num;

    @SerializedName("price")
    private double price;

    public CategoryResponseModel(int id, String name, String prestations_num, double price) {
        this.id = id;
        this.name = name;
        this.prestations_num = prestations_num;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrestations_num() {
        return prestations_num;
    }

    public void setPrestations_num(String prestations_num) {
        this.prestations_num = prestations_num;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
