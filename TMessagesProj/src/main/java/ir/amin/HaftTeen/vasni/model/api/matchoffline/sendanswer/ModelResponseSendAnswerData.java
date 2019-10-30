package ir.amin.HaftTeen.vasni.model.api.matchoffline.sendanswer;

public class ModelResponseSendAnswerData {
    private int life_match;
    private int user_life;
    private String correct_answers;
    private int type;

    public int getLife_match() {
        return this.life_match;
    }

    public void setLife_match(int life_match) {
        this.life_match = life_match;
    }

    public int getUser_life() {
        return this.user_life;
    }

    public void setUser_life(int user_life) {
        this.user_life = user_life;
    }

    public String getCorrect_answers() {
        return this.correct_answers;
    }

    public void setCorrect_answers(String correct_answers) {
        this.correct_answers = correct_answers;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
