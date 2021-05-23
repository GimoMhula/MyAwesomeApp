package dev.visum.demoapp.model;

import com.google.gson.annotations.SerializedName;

public class SaleAddedResponseModel {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private SaleAddedModel data;

    public SaleAddedResponseModel(boolean success, String message, SaleAddedModel data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SaleAddedModel getData() {
        return data;
    }

    public void setData(SaleAddedModel data) {
        this.data = data;
    }
}
