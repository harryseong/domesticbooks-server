package com.harryseong.mybookrepo.resources.domain;

public class BarCodeInfo {
    private static String text;
    private static String format;

    public String getText() {
        return text;
    }

    public String getFormat() {
        return format;
    }

    public BarCodeInfo() {
    }

    public BarCodeInfo(String text, String format) {
        this.text = text;
        this.format = format;
    }
}
