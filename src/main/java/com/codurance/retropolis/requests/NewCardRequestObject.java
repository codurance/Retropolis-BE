package com.codurance.retropolis.requests;

public class NewCardRequestObject {

    private String text;

    public NewCardRequestObject() {
    }

    public NewCardRequestObject(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
