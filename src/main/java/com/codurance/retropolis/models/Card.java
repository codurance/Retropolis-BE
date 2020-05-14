package com.codurance.retropolis.models;

public class Card {
    public final int id;
    private String body;

    public Card(String body, int id) {
        this.id = id;
        this.body = body;
    }

    public String getBody() {
        return body;
    }
}
