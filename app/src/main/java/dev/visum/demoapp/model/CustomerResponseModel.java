package dev.visum.demoapp.model;

import com.google.gson.annotations.SerializedName;

public class CustomerResponseModel {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("contact")
    private String contact;

    @SerializedName("signature")
    private String signature;

    public CustomerResponseModel(String id, String name, String contact, String signature) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.signature = signature;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "CustomerResponseModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", contact='" + contact + '\'' +
                '}';
    }
}
