package dev.visum.demoapp.model;

import com.google.gson.annotations.SerializedName;

public class ResponseModel<T> {

    @SerializedName("response")
    private T response;

    public ResponseModel(T response) {
        this.response = response;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "ResponseModel{" +
                "response=" + response +
                '}';
    }
}
