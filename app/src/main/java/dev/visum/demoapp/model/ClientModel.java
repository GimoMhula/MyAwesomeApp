package dev.visum.demoapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ClientModel implements Serializable {

    private String id;
    private String name;
    private String address;
    private String email;
    private String contact;
    private String signature;
    private String created_at;
    private String updated_at;

    public ClientModel(String id, String name, String contact) {
        this.id = id;
        this.name = name;
        this.contact = contact;
    }

    public ClientModel(String id, String name, String address, String email, String contact, String signature, String created_at, String updated_at) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.contact = contact;
        this.signature = signature;
        this.created_at = created_at;
        this.updated_at = updated_at;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
