package dev.visum.demoapp.model;

import java.io.Serializable;

public class SoldItem implements Serializable {
    private String id;
    private String title;
    private String subtitle;
    private String date;
    private String imgUrl;
    private double remain;
    private boolean containsPrest;

    public SoldItem(String id, String title, String subtitle, String date, String imgUrl, double remain, boolean containsPrest) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.date = date;
        this.imgUrl = imgUrl;
        this.remain = remain;
        this.containsPrest = containsPrest;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public double getRemain() {
        return remain;
    }

    public void setRemain(double remain) {
        this.remain = remain;
    }

    public boolean isContainsPrest() {
        return containsPrest;
    }

    public void setContainsPrest(boolean containsPrest) {
        this.containsPrest = containsPrest;
    }
}
