package dev.visum.demoapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Provinces implements Parcelable {


    public static final Creator<Provinces> CREATOR = new Creator<Provinces>() {
        @Override
        public Provinces createFromParcel(Parcel in) {
            return new Provinces(in);
        }

        @Override
        public Provinces[] newArray(int size) {
            return new Provinces[size];
        }
    };
    @SerializedName("success")
    private boolean success;
    @SerializedName("data")
    private List<Province> provinces = new ArrayList<>();

    public Provinces() {
    }

    protected Provinces(Parcel in) {
        success = in.readByte() != 0;
        provinces = in.createTypedArrayList(Province.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (success ? 1 : 0));
        dest.writeTypedList(provinces);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Province> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<Province> provinces) {
        this.provinces = provinces;
    }
}
