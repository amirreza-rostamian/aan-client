package ir.amin.HaftTeen.vasni.model.api.splash;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseModelSplashApi {
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("data")
    @Expose
    private ResponseModelSplashApiData data;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public ResponseModelSplashApiData getData() {
        return data;
    }

    public void setData(ResponseModelSplashApiData data) {
        this.data = data;
    }
}
