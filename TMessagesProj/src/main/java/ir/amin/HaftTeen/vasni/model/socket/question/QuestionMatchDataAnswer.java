package ir.amin.HaftTeen.vasni.model.socket.question;

public class QuestionMatchDataAnswer {

    private String a;
    private String b;
    private String c;

    private String v1;
    private String v2;
    private String v3;

    public QuestionMatchDataAnswer(String a, String b, String c, String v1, String v2, String v3) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }


    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getV1() {
        return v1;
    }

    public void setV1(String v1) {
        this.v1 = v1;
    }

    public String getV2() {
        return v2;
    }

    public void setV2(String v2) {
        this.v2 = v2;
    }

    public String getV3() {
        return v3;
    }

    public void setV3(String v3) {
        this.v3 = v3;
    }
}
