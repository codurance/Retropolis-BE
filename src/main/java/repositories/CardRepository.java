package repositories;

import models.Card;

import java.util.List;

public interface CardRepository {
    List<Card> getAll();

    void save(Card body);
}
