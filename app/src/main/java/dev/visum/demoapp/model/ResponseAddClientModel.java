package dev.visum.demoapp.model;

import com.google.gson.annotations.SerializedName;

public class ResponseAddClientModel {
    @SerializedName("success")
    private String success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private CustomerResponseModel data;

    public ResponseAddClientModel(String success, String message, CustomerResponseModel data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CustomerResponseModel getData() {
        return data;
    }

    public void setData(CustomerResponseModel data) {
        this.data = data;
    }
}
