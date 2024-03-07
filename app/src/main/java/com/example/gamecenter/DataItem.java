package com.example.gamecenter;

public class DataItem {
    private String text;
    private int imageResource;
    private String record;

    public DataItem(String text, int imageResource, String record) {
        this.text = text;
        this.imageResource = imageResource;
        this.record = record;
    }

    public String getText() {
        return text;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getRecord() {
        return record;
    }
}
