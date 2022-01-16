package dev.visum.demoapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Province implements Parcelable {


    public static final Creator<Province> CREATOR = new Creator<Province>() {
        @Override
        public Province createFromParcel(Parcel in) {
            return new Province(in);
        }

        @Override
        public Province[] newArray(int size) {
            return new Province[size];
        }
    };
    private int id;
    private String name;
    private double total_price;
    private double promo_price;
    private double free_price;
    private int category_id;
    private String created_at;
    private String updated_at;
    private Category category;


    public Province() {
    }

    public Province(int id, String name, double total_price, double promo_price, double free_price, int category_id, String created_at, String updated_at, Category category) {
        this.id = id;
        this.name = name;
        this.total_price = total_price;
        this.promo_price = promo_price;
        this.free_price = free_price;
        this.category_id = category_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.category = category;
    }

    protected Province(Parcel in) {
        id = in.readInt();
        name = in.readString();
        total_price = in.readDouble();
        promo_price = in.readDouble();
        free_price = in.readDouble();
        category_id = in.readInt();
        created_at = in.readString();
        updated_at = in.readString();


    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeDouble(total_price);
        dest.writeDouble(promo_price);
        dest.writeDouble(free_price);
        dest.writeInt(category_id);
        dest.writeString(created_at);
        dest.writeString(updated_at);
    }

    public static Creator<Province> getCREATOR() {
        return CREATOR;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public double getPromo_price() {
        return promo_price;
    }

    public void setPromo_price(double promo_price) {
        this.promo_price = promo_price;
    }

    public double getFree_price() {
        return free_price;
    }

    public void setFree_price(double free_price) {
        this.free_price = free_price;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
