package com.purva.nits.spokenenglishapp;

public class DialogueModel {
    int conversationId;
    int sentenceId;
    String sentence;
    String person;
    public DialogueModel(){ }
    public DialogueModel(int conversationId, int sentenceId, String sentence, String person){
        this.conversationId =conversationId;
        this.sentenceId =sentenceId;
        this.sentence=sentence;
        this.person=person;
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public int getSentenceId() {
        return sentenceId;
    }

    public void setSentenceId(int sentenceId) {
        this.sentenceId = sentenceId;
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
