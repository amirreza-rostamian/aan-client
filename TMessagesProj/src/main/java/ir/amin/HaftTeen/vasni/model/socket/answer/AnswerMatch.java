package ir.amin.HaftTeen.vasni.model.socket.answer;

public class AnswerMatch {
    private int correct;
    private AnswerMatchData[] data;
    private String type;

    public int getCorrect() {
        return this.correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public AnswerMatchData[] getData() {
        return this.data;
    }

    public void setData(AnswerMatchData[] data) {
        this.data = data;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
