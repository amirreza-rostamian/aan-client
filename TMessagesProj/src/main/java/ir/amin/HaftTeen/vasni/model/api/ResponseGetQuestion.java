package ir.amin.HaftTeen.vasni.model.api;

import java.util.ArrayList;

public class ResponseGetQuestion {
    private ArrayList<ResponseGetQuestionData> data;
    private int success;

    public ArrayList<ResponseGetQuestionData> getData() {
        return data;
    }

    public void setData(ArrayList<ResponseGetQuestionData> data) {
        this.data = data;
    }

    public int getSuccess() {
        return this.success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
