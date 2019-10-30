package ir.amin.HaftTeen.vasni.model.api.listquestionmatchoffline;

public class ResponseListQuestionMatchOffline {
    private ResponseListQuestionMatchOfflineData data;
    private int success;
    private ResponseListQuestionMatchOfflineErrors errors;

    public ResponseListQuestionMatchOfflineData getData() {
        return this.data;
    }

    public void setData(ResponseListQuestionMatchOfflineData data) {
        this.data = data;
    }

    public int getSuccess() {
        return this.success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public ResponseListQuestionMatchOfflineErrors getErrors() {
        return errors;
    }

    public void setErrors(ResponseListQuestionMatchOfflineErrors errors) {
        this.errors = errors;
    }
}
