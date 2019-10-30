package ir.amin.HaftTeen.vasni.model.api.matchoffline.sendanswer;

/**
 * created by Hossein Rezaeian on 30/01/2019 . Tel: 0915-888-2204
 */
public class ModelSendAnswerOffline {
    private String question_id;
    private String answer_id;

    public ModelSendAnswerOffline(String question_id, String answer_id) {
        this.question_id = question_id;
        this.answer_id = answer_id;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(String answer_id) {
        this.answer_id = answer_id;
    }
}
