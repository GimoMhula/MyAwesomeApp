package dev.visum.demoapp.model;

import com.google.gson.annotations.SerializedName;

public class MySaleModel {
    @SerializedName("id")
    private double id;

    @SerializedName("total")
    private double total;

    @SerializedName("remain")
    private double remain;

    @SerializedName("image")
    private String image;

    @SerializedName("name")
    private String name;

    @SerializedName("quant")
    private double quant;

    @SerializedName("price")
    private double price;

    @SerializedName("completed")
    private double completed;

    @SerializedName("warehouse")
    private String warehouse;

    @SerializedName("saleDate")
    private String saleDate;

    @SerializedName("totalPrest")
    private double totalPrest;

    public MySaleModel(double id, double total, double remain, String image, String name, double quant, double price, double completed, String warehouse, String saleDate, double totalPrest) {
        this.id = id;
        this.total = total;
        this.remain = remain;
        this.image = image;
        this.name = name;
        this.quant = quant;
        this.price = price;
        this.completed = completed;
        this.warehouse = warehouse;
        this.saleDate = saleDate;
        this.totalPrest = totalPrest;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getRemain() {
        return remain;
    }

    public void setRemain(double remain) {
        this.remain = remain;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuant() {
        return quant;
    }

    public void setQuant(double quant) {
        this.quant = quant;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCompleted() {
        return completed;
    }

    public void setCompleted(double completed) {
        this.completed = completed;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }

    public double getTotalPrest() {
        return totalPrest;
    }

    public void setTotalPrest(double totalPrest) {
        this.totalPrest = totalPrest;
    }
}
