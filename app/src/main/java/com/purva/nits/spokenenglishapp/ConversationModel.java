package com.purva.nits.spokenenglishapp;

public class ConversationModel {
    int convid;
    int sentid;
    String sentence;
    String person;
    public ConversationModel(){ }
    public ConversationModel(int convid, int sentid, String sentence, String person){
        this.convid=convid;
        this.sentid=sentid;
        this.sentence=sentence;
        this.person=person;
    }

    public int getConvid() {
        return convid;
    }

    public void setConvid(int convid) {
        this.convid = convid;
    }

    public int getSentid() {
        return sentid;
    }

    public void setSentid(int sentid) {
        this.sentid = sentid;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }
}
