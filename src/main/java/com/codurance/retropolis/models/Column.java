package com.codurance.retropolis.models;

import java.util.List;

public class Column {
    private int id;
    private String title;

    private List<Card> cards;

    public Column(int id, String title, List<Card> cards) {
        this.id = id;
        this.title = title;
        this.cards = cards;
    }

    public Column() {
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<Card> getCards() {
        return cards;
    }
}
