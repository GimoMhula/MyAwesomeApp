package dev.visum.demoapp.model;

public class SoldItem {
    private String id;
    public String title;
    public String subtitle;
    public String date;
    public String imgUrl;

    public SoldItem(String id, String title, String subtitle, String date, String imgUrl) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.date = date;
        this.imgUrl = imgUrl;
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
}
