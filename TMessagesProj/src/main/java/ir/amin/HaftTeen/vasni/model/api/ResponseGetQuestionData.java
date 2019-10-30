package ir.amin.HaftTeen.vasni.model.api;

import java.io.Serializable;
import java.util.ArrayList;

public class ResponseGetQuestionData implements Serializable {
    private String thumbnail;
    private String decscripton;
    private String faq;
    private String name;
    private String start;
    private ArrayList<ResponseGetQuestionDataQuestions> questions;
    private boolean isOnline;
    private String end;
    private int id;
    private String link;
    private String onlineTime;
    private int program_id;
    private String program;
    private boolean isBot = false;


    public String getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(String onlineTime) {
        this.onlineTime = onlineTime;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDecscripton() {
        return this.decscripton;
    }

    public void setDecscripton(String decscripton) {
        this.decscripton = decscripton;
    }

    public String getFaq() {
        return this.faq;
    }

    public void setFaq(String faq) {
        this.faq = faq;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart() {
        return this.start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public ArrayList<ResponseGetQuestionDataQuestions> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<ResponseGetQuestionDataQuestions> questions) {
        this.questions = questions;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public boolean getIsOnline() {
        return this.isOnline;
    }

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public String getEnd() {
        return this.end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getProgram_id() {
        return program_id;
    }

    public void setProgram_id(int program_id) {
        this.program_id = program_id;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public boolean isBot() {
        return isBot;
    }

    public void setBot(boolean bot) {
        isBot = bot;
    }
}
