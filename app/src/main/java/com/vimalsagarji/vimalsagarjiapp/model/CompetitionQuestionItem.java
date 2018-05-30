package com.vimalsagarji.vimalsagarjiapp.model;

@SuppressWarnings("ALL")
public class CompetitionQuestionItem {
    private String id,question,qtype,competition_id,answer,options;

    public CompetitionQuestionItem(String id, String question, String qtype, String competition_id, String answer, String options) {
        this.id = id;
        this.question = question;
        this.qtype = qtype;
        this.competition_id = competition_id;
        this.answer = answer;
        this.options = options;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQtype() {
        return qtype;
    }

    public void setQtype(String qtype) {
        this.qtype = qtype;
    }

    public String getCompetition_id() {
        return competition_id;
    }

    public void setCompetition_id(String competition_id) {
        this.competition_id = competition_id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }
}
