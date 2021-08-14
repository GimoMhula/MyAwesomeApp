package dev.visum.demoapp.model;

public class SurveyAnswerModel {

    public int survey_id;
    public int question_id;
    public int customer_id;
    public String answer;
    public int agent_id;
    public String updated_at;


    public SurveyAnswerModel(int survey_id, int question_id, int customer_id, String answer, int agent_id, String updated_at) {
        this.survey_id = survey_id;
        this.question_id = question_id;
        this.customer_id = customer_id;
        this.answer = answer;
        this.agent_id = agent_id;
        this.updated_at = updated_at;
    }

    public SurveyAnswerModel(int question_id, int customer_id, String answer, int agent_id) {
        this.question_id = question_id;
        this.customer_id = customer_id;
        this.answer = answer;
        this.agent_id = agent_id;

    }

    @Override
    public String toString() {
        return "SurveyAnswerModel{" +
                "survey_id=" + survey_id +
                ", question_id=" + question_id +
                ", customer_id=" + customer_id +
                ", answer='" + answer + '\'' +
                ", agent_id=" + agent_id +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
}
