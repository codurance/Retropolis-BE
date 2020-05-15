package com.codurance.retropolis.models;

public class Card {
    public final int id;
    private String text;

    public Card(String text, int id) {
        this.id = id;
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
