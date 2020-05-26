package com.codurance.retropolis.services;

import com.codurance.retropolis.factories.CardFactory;
import com.codurance.retropolis.models.Card;
import com.codurance.retropolis.repositories.CardRepository;
import com.codurance.retropolis.requests.NewCardRequestObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardService {

  private final CardFactory cardFactory;
  private final CardRepository cardRepository;

  @Autowired
  public CardService(CardFactory cardFactory, CardRepository cardRepository) {
    this.cardFactory = cardFactory;
    this.cardRepository = cardRepository;
  }

  public Card addCard(NewCardRequestObject requestObject) {
    Card newCard = cardFactory.create(requestObject);
    return cardRepository.insert(newCard);
  }
}
