package dev.visum.demoapp.model;

import com.google.gson.annotations.SerializedName;

public class Warehouse {

    @SerializedName("name")
    public String name ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
