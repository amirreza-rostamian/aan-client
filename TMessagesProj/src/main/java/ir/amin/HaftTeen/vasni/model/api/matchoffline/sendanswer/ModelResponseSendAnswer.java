package ir.amin.HaftTeen.vasni.model.api.matchoffline.sendanswer;

public class ModelResponseSendAnswer {
    private ModelResponseSendAnswerData data;
    private int success;

    public ModelResponseSendAnswerData getData() {
        return this.data;
    }

    public void setData(ModelResponseSendAnswerData data) {
        this.data = data;
    }

    public int getSuccess() {
        return this.success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
