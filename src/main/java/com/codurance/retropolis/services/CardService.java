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
  private final BoardService boardService;
  private CardRepository cardRepository;

  public CardService(CardFactory cardFactory, BoardService boardService) {
    this.boardService = boardService;
    this.cardFactory = cardFactory;
  }

  @Autowired
  public CardService(CardFactory cardFactory, BoardService boardService, CardRepository cardRepository) {
    this.cardFactory = cardFactory;
    this.boardService = boardService;
    this.cardRepository = cardRepository;
  }

  public Card addCard(NewCardRequestObject requestObject) {
    Card card = cardFactory.create(requestObject);
    return boardService.addCard(insert(card));
  }

  private Card insert(Card newCard){

    return cardRepository.insert(newCard);
  }
}
