package dev.visum.demoapp.model;

import com.google.gson.annotations.SerializedName;

public class AddClientModel {
    @SerializedName("name")
    private String name;

    @SerializedName("address")
    private String address;

    @SerializedName("contact")
    private String contact;

    @SerializedName("gender")
    private String gender;

    @SerializedName("birthday")
    private String birthday;


    public AddClientModel(String name, String address, String contact, String gender, String birthday) {
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.gender = gender;
        this.birthday = birthday;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
