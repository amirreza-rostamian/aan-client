package ir.amin.HaftTeen.vasni.model.api;


import java.util.ArrayList;

import ir.amin.HaftTeen.vasni.model.api.bots.ResponseGetBotsData;

public class ResponseGetBots {
    private ArrayList<ResponseGetBotsData> data;
    private int success;


    public ArrayList<ResponseGetBotsData> getData() {
        return data;
    }

    public void setData(ArrayList<ResponseGetBotsData> data) {
        this.data = data;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
