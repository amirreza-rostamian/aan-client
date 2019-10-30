package ir.amin.HaftTeen.vasni.model.socket.question;

public class QuestionMatchData {
    private String question;
    private String total;
    private String star;
    private int correct = -1;
    private QuestionMatchDataAnswer answer;

    public QuestionMatchData(String question, String total, String star, int correct, QuestionMatchDataAnswer answer) {
        this.question = question;
        this.total = total;
        this.star = star;
        this.correct = correct;
        this.answer = answer;
    }

    public String getQuestion() {
        return this.question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public QuestionMatchDataAnswer getAnswer() {
        return this.answer;
    }

    public void setAnswer(QuestionMatchDataAnswer answer) {
        this.answer = answer;
    }


    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }
}
