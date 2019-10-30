package ir.amin.HaftTeen.vasni.model.api.playerchart;

import java.util.ArrayList;

public class ResponseChart {
    private ArrayList<ResponseChartData> data;
    private int success;

    public ArrayList<ResponseChartData> getData() {
        return data;
    }

    public void setData(ArrayList<ResponseChartData> data) {
        this.data = data;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
