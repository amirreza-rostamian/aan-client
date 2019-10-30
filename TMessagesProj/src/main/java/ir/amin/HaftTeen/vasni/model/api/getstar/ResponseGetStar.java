package ir.amin.HaftTeen.vasni.model.api.getstar;

public class ResponseGetStar {
    private ResponseGetStarData data;
    private int success;

    public ResponseGetStarData getData() {
        return this.data;
    }

    public void setData(ResponseGetStarData data) {
        this.data = data;
    }

    public int getSuccess() {
        return this.success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
