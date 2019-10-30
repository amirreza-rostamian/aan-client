package ir.amin.HaftTeen.vasni.model.api.listquestionmatchoffline;

import java.util.ArrayList;

import ir.amin.HaftTeen.vasni.model.api.ResponseGetQuestionDataQuestions;

public class ResponseListQuestionMatchOfflineData {
    private boolean repeat;
    private ArrayList<ResponseGetQuestionDataQuestions> questions;

    public boolean getRepeat() {
        return this.repeat;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public ArrayList<ResponseGetQuestionDataQuestions> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<ResponseGetQuestionDataQuestions> questions) {
        this.questions = questions;
    }
}
