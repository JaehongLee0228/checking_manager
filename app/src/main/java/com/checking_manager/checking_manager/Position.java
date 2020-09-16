package com.checking_manager.checking_manager;

public class Position {

    private String position;
    private String checked_complete;

    public Position(String position, String checked_complete) {
        this.position = position;
        this.checked_complete = checked_complete;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getChecked_complete() {
        return checked_complete;
    }

    public void setChecked_complete(String checked_complete) {
        this.checked_complete = checked_complete;
    }
}
