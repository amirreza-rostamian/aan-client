package ir.amin.HaftTeen.vasni.model.api.register;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseRegister implements Serializable {
    @Expose
    private Integer success;
    @SerializedName("data")
    @Expose
    private ResponseRegisterData data;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public ResponseRegisterData getData() {
        return data;
    }

    public void setData(ResponseRegisterData data) {
        this.data = data;
    }
}
