package com.checking_manager.checking_manager.checking_sequence;

import java.util.Map;

public class wholeResultTable {
    Map contents;
    String date;

    public Map getContents() {
        return contents;
    }

    public void setContents(Map contents) {
        this.contents = contents;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public wholeResultTable(Map contents, String date) {
        this.contents = contents;
        this.date = date;
    }

}
