package com.example.ljh_final_exam;

import java.io.Serializable;

public class DiaryDTO implements Serializable {
    private int key;
    private String sentence;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }
}
