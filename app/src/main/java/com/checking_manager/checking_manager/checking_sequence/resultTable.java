package com.checking_manager.checking_manager.checking_sequence;

public class resultTable {

    //public String contents;
    public String okay;
    public String special;

    public resultTable(String okay, String special) {
        this.okay = okay;
        this.special = special;
    }


    public String getOkay() {
        return okay;
    }

    public void setOkay(String okay) {
        this.okay = okay;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }


}
