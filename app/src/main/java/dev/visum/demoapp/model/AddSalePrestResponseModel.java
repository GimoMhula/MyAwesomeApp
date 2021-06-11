package dev.visum.demoapp.model;

import com.google.gson.annotations.SerializedName;

public class AddSalePrestResponseModel {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    public AddSalePrestResponseModel(boolean success, String message) {
        this.success = success;
        this.message = message;
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
}
