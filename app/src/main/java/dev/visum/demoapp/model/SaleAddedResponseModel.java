package dev.visum.demoapp.model;

import com.google.gson.annotations.SerializedName;

public class SaleAddedResponseModel {

    @SerializedName("success")
    private boolean success;

    @SerializedName("url")
    private String url;

    public SaleAddedResponseModel(boolean success, String url) {
        this.success = success;
        this.url = url;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /*@SerializedName("response")
    private SaleAddedModel response;

    public SaleAddedResponseModel(boolean success, String message, SaleAddedModel response) {
        this.success = success;
        this.message = message;
        this.response = response;
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

    public SaleAddedModel getResponse() {
        return response;
    }

    public void setResponse(SaleAddedModel response) {
        this.response = response;
    }*/
}
