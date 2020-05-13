package com.codurance.retropolis.repositories;

import com.codurance.retropolis.models.Card;

import java.util.List;

public interface CardRepository {
    List<Card> getAll();

    void save(Card body);
}
