package dev.visum.demoapp.model;

public class SurveyModel {

  public int id;
  public String title;
  public String description;
  public String status;
  public String created_at;
  public String updated_at;

    public SurveyModel(int id, String title, String description, String status, String created_at, String updated_at) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public SurveyModel(int id, String title, String description, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public SurveyModel(String title, String description, String status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }
}
