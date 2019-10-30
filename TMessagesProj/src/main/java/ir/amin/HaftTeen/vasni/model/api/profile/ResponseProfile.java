package ir.amin.HaftTeen.vasni.model.api.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseProfile implements Serializable {
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("data")
    @Expose
    private ResponseProfileData data;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public ResponseProfileData getData() {
        return data;
    }

    public void setData(ResponseProfileData data) {
        this.data = data;
    }
}
