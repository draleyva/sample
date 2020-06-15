/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dleyva.app;

import ch.qos.logback.classic.Logger;
import com.dleyva.app.Card.Suit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author David Ricardo
 */
public class Hand
{
  public static Logger LOGGER = (Logger)org.slf4j.LoggerFactory.getLogger(Hand.class);
  
  public enum PlayRank
  {
    None(0),
    HighCard(1),
    OnePair(2),
    TwoPairs(3),
    ThreeofaKind(4),
    Straight(5),
    Flush(6),
    FullHouse(7),
    FourofaKind(8),
    StraigthFlush(9),
    RoyalFlush(10);
    
    private final int rank;
    
    public final int getRank()
    {
      return rank;
    }
    
    PlayRank(int r)
    {
      rank = r;
    }
  }
  
  List<Card> hand;
  boolean sameSuit = true;
  Suit lastSuit;
  
  public Hand()
  {
    hand = new ArrayList<>();
  }
  
  public void addCard(Card card)
  {
    if(lastSuit != null && lastSuit != card.getSuit())
      sameSuit = false;
    
    lastSuit = card.getSuit();
    
    hand.add(card);
  }
  
  public static class Evaluation
  {
    public Evaluation(PlayRank pr, Card c)
    {
      playRank = pr;
      card = c;
    }
    /**
     * @return the playRank
     */
    public PlayRank getPlayRank() {
      return playRank;
    }

    /**
     * @param playRank the playRank to set
     */
    public void setPlayRank(PlayRank playRank) {
      this.playRank = playRank;
    }

    /**
     * @return the card
     */
    public Card getCard() {
      return card;
    }

    /**
     * @param card the card to set
     */
    public void setCard(Card card) {
      this.card = card;
    }
    private PlayRank playRank;
    private Card card;
    
  }
  
  public static class Counter
  {
    public Counter()
    {
    }
    
    public void increment()
    {
      ++counter;
    }
    /**
     * @return the counter
     */
    public int getCounter() {
      return counter;
    }

    /**
     * @param counter the counter to set
     */
    public void setCounter(int counter) {
      this.counter = counter;
    }

    /**
     * @return the card
     */
    public Card getCard() {
      return card;
    }

    /**
     * @param card the card to set
     */
    public void setCard(Card card) {
      this.card = card;
    }
    private int counter = 0;
    private Card card = null;
  }
    
  public Evaluation evaluate()
  {
    Counter [] counter = new Counter[15];
    
    for(int i = 0; i < counter.length; ++i)
      counter[i] = new Counter();
    
    Collections.sort(hand, Card.rankComparator);
    
    Card lastcard = hand.get(0);
    
    for(int rec = 1; rec < hand.size(); ++rec)
    {
      if(lastcard.getRank()+1 == hand.get(rec).getRank())
        lastcard = hand.get(rec);
      else
      {
        lastcard = null;
        break;
      }
    }
    
    boolean sequential = lastcard != null;
    
    if(sequential)
    {
      lastcard = hand.get(0);
      
      if(sameSuit)
      {
        return new Evaluation(lastcard.getRank() == 10?PlayRank.RoyalFlush:PlayRank.StraigthFlush, lastcard);
      } 
    }
    
    for(int rec = 1; rec < hand.size(); ++rec)
    {
      counter[hand.get(rec).getRank()].setCard(hand.get(rec));
      counter[hand.get(rec).getRank()].increment();
    }
    
    int paircounter = 0;
    boolean three = false;
    Card paircard = null;
    
    for(int rec = 0; rec < counter.length; ++rec)
    {
      switch(counter[rec].getCounter())
      {
        case 4:
          return new Evaluation(PlayRank.FourofaKind, counter[rec].getCard());
        case 2:
          ++paircounter;
          paircard = counter[rec].getCard();
        break;
        case 3:
          three = true;
          lastcard = counter[rec].getCard();
        break;
      }
    }
    
    if(paircounter == 1 && three)
      return new Evaluation(PlayRank.FullHouse, lastcard);
    
    if(sameSuit)
      return new Evaluation(PlayRank.Flush, getTopCard());
    
    if(sequential)
      return new Evaluation(PlayRank.Straight, getTopCard());
    
    if(three)
      return new Evaluation(PlayRank.ThreeofaKind, lastcard);
    
    switch(paircounter)
    {
      case 2:
        return new Evaluation(PlayRank.TwoPairs, paircard);
      case 1:
        return new Evaluation(PlayRank.OnePair, paircard);
    }
    
    return new Evaluation(PlayRank.None, getTopCard());
  }
  
  public Card getTopCard()
  {    
    return hand.get(hand.size()-1);   
  }
  
  public Card getReverseTopCard()
  {    
    Card card = hand.get(hand.size()-1);
    int i;
    
    for(i = hand.size()-1; i >=0 ; --i)
    {
      if(hand.get(i).compareTo(card) < 0)
        break;
      card = hand.get(i);
    }
    
    return hand.get(i);
  }
  
  public String show()
  {
    StringBuilder sb = new StringBuilder();
    
    for(Card card : hand)
    {
      if(sb.length() > 0)
        sb.append(" ");
      
      sb.append(card.getRaw());
    }
    
    return sb.toString();
  }
}
