package dev.visum.demoapp.model;

import com.google.gson.annotations.SerializedName;

public class ProductResponseModel {

    @SerializedName("id")
    private int id;

    @SerializedName("category_id")
    private String category_id;

    @SerializedName("name")
    private String name;

    @SerializedName("price")
    private double price;

    @SerializedName("image")
    private String image;

    @SerializedName("qty")
    private int qty;

    @SerializedName("category")
    private CategoryResponseModel category;

    public ProductResponseModel(int id, String category_id, String name, double price, String image, int qty, CategoryResponseModel category) {
        this.id = id;
        this.category_id = category_id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.qty = qty;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public CategoryResponseModel getCategory() {
        return category;
    }

    public void setCategory(CategoryResponseModel category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "ProductResponseModel{" +
                "id=" + id +
                ", category_id='" + category_id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", image='" + image + '\'' +
                ", qty=" + qty +
                ", category=" + category +
                '}';
    }
}
