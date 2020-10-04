package com.checking_manager.checking_manager;

public class Contents {

    private String special, okay, content;

    public Contents(String content, String okay, String special) {
        this.content = content;
        this.special = special;
        this.okay = okay;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getOkay() {
        return okay;
    }

    public void setOkay(String okay) {
        this.okay = okay;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
