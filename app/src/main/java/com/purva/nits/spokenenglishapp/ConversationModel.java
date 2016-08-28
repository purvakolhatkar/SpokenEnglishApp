package com.purva.nits.spokenenglishapp;

public class ConversationModel {
    int conversationId;
    String type;
    String title;
    public ConversationModel(){}
    public ConversationModel(int conversationId, String type, String title) {
        this.conversationId = conversationId;
        this.type = type;
        this.title = title;
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
