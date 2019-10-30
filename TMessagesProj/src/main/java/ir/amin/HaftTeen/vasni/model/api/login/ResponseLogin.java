package ir.amin.HaftTeen.vasni.model.api.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseLogin implements Serializable {
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("data")
    @Expose
    private ResponseLoginData data;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public ResponseLoginData getData() {
        return data;
    }

    public void setData(ResponseLoginData data) {
        this.data = data;
    }
}
