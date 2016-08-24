package com.purva.nits.spokenenglishapp;

public class ConversationidModel {
    int convid;
    String type;
    String title;
    public ConversationidModel(){}
    public ConversationidModel(int convid, String type, String title) {
        this.convid = convid;
        this.type = type;
        this.title = title;
    }

    public int getConvid() {
        return convid;
    }

    public void setConvid(int convid) {
        this.convid = convid;
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
