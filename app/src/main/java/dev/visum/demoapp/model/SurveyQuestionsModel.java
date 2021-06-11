package dev.visum.demoapp.model;

public class SurveyQuestionsModel {

  public int id;
  public String title;
  public String description;
  public int type;

    public SurveyQuestionsModel(int id, String title, String description, int type) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
    }
}
