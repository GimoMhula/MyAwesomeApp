package dev.visum.demoapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MySaleModel {
    @SerializedName("id")
    private String id;

    @SerializedName("product_id")
    private String product_id;

    @SerializedName("agent_id")
    private String agent_id;

    @SerializedName("customer_id")
    private String customer_id;

    @SerializedName("quant")
    private int quant;

    @SerializedName("missing")
    private double missing;

    @SerializedName("totalPrice")
    private double totalPrice;

    @SerializedName("done")
    private int done;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("prestation")
    private ArrayList<MySalesPrestModel> prestation;

    @SerializedName("product")
    private ProductResponseModel product;

    public MySaleModel(String id, String product_id, String agent_id, String customer_id, int quant, double missing, double totalPrice, int done, String created_at, ArrayList<MySalesPrestModel> prestation, ProductResponseModel product) {
        this.id = id;
        this.product_id = product_id;
        this.agent_id = agent_id;
        this.customer_id = customer_id;
        this.quant = quant;
        this.missing = missing;
        this.totalPrice = totalPrice;
        this.done = done;
        this.created_at = created_at;
        this.prestation = prestation;
        this.product = product;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
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

    public int getQuant() {
        return quant;
    }

    public void setQuant(int quant) {
        this.quant = quant;
    }

    public double getMissing() {
        return missing;
    }

    public void setMissing(double missing) {
        this.missing = missing;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public ArrayList<MySalesPrestModel> getPrestation() {
        return prestation;
    }

    public void setPrestation(ArrayList<MySalesPrestModel> prestation) {
        this.prestation = prestation;
    }

    public ProductResponseModel getProduct() {
        return product;
    }

    public void setProduct(ProductResponseModel product) {
        this.product = product;
    }
}
