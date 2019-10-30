package ir.amin.HaftTeen.vasni.model.api;

import com.google.gson.annotations.SerializedName;

public class ResponseGetQuestionDataQuestions {
    @SerializedName("1")
    private String a1;
    @SerializedName("2")
    private String a2;
    @SerializedName("3")
    private String a3;
    @SerializedName("4")
    private String a4;
    private String question;
    private String correct_answer_id;
    private int id;
    private int star;


    public String getA1() {
        return this.a1;
    }

    public void setA1(String a1) {
        this.a1 = a1;
    }

    public String getA2() {
        return this.a2;
    }

    public void setA2(String a2) {
        this.a2 = a2;
    }

    public String getA3() {
        return this.a3;
    }

    public void setA3(String a3) {
        this.a3 = a3;
    }

    public String getA4() {
        return this.a4;
    }

    public void setA4(String a4) {
        this.a4 = a4;
    }

    public String getQuestion() {
        return this.question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrect_answer_id() {
        return this.correct_answer_id;
    }

    public void setCorrect_answer_id(String correct_answer_id) {
        this.correct_answer_id = correct_answer_id;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }
}
