package com.example.gamecenter;

public class DataItem {
    private String text;
    private int imageResource;

    public DataItem(String text, int imageResource) {
        this.text = text;
        this.imageResource = imageResource;
    }

    public String getText() {
        return text;
    }

    public int getImageResource() {
        return imageResource;
    }
}
