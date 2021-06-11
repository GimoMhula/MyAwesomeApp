package dev.visum.demoapp.model;

import com.google.gson.annotations.SerializedName;

public class AddSaleResponseModel {
    @SerializedName("success")
    private boolean success;

    public AddSaleResponseModel(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
