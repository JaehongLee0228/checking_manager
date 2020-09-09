package com.checking_manager.checking_manager;

public class stuff {

    private String kind_of_stuff;
    private int the_number_of_total;
    private int the_number_of_checked;

    public stuff(String kind_of_stuff, int the_number_of_total, int the_number_of_checked) {
        this.kind_of_stuff = kind_of_stuff;
        this.the_number_of_total = the_number_of_total;
        this.the_number_of_checked = the_number_of_checked;
    }

    public String getKind_of_stuff() {
        return kind_of_stuff;
    }

    public void setKind_of_stuff(String kind_of_stuff) {
        this.kind_of_stuff = kind_of_stuff;
    }

    public int getThe_number_of_total() {
        return the_number_of_total;
    }

    public void setThe_number_of_total(int the_number_of_total) {
        this.the_number_of_total = the_number_of_total;
    }

    public int getThe_number_of_checked() {
        return the_number_of_checked;
    }

    public void setThe_number_of_checked(int the_number_of_checked) {
        this.the_number_of_checked = the_number_of_checked;
    }
}
