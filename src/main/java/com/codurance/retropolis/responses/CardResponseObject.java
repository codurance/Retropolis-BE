package com.codurance.retropolis.responses;

public class CardResponseObject {

  private String text;
  private Long cardId;
  private Long columnId;
  private Boolean haveVoted;
  private Integer totalVoters;

  public CardResponseObject() {
  }

  public CardResponseObject(String text, Long cardId, Long columnId, Boolean haveVoted,
      Integer totalVoters) {
    this.text = text;
    this.cardId = cardId;
    this.columnId = columnId;
    this.haveVoted = haveVoted;
    this.totalVoters = totalVoters;
  }

  public String getText() {
    return text;
  }

  public Long getCardId() {
    return cardId;
  }

  public Long getColumnId() {
    return columnId;
  }

  public Boolean getHaveVoted() {
    return haveVoted;
  }

  public Integer getTotalVoters() {
    return totalVoters;
  }
}
