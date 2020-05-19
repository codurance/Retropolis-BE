package com.codurance.retropolis.requests;

public class NewCardRequestObject {

    private String text;
    private int columnId;

    public NewCardRequestObject(String text, int columnId) {
        this.text = text;
        this.columnId = columnId;
    }

    public NewCardRequestObject(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public int getColumnId() {
        return columnId;
    }
}
