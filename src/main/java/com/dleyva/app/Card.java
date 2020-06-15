/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dleyva.app;

import java.util.Comparator;

/**
 *
 * @author David Ricardo
 */
public class Card 
{  
  public enum Suit
  {
    H("H", "hearts"),
    S("S", "spades"), 
    D("D", "diamonds"),
    C("C", "clubs");
    
    private final String suit;
    private final String description;
    
    Suit(String s, String n)
    {
      suit = s;
      description = n;
    }
    
    public static Suit fromByte(byte b)
    {
      switch(b)
      {
        case 'H':
          return H;
        case 'S':
          return S;
        case 'D':
          return D;
        case 'C':
          return C;
      }
      
      return null;
    }
  }
  
  private int rank;
  private Suit suit;
  private String raw;
  
  public Card(String raw)
  {
    this.raw = raw;
    
    byte [] data = raw.getBytes();
    
    if(data[0] >= (byte)'1' && data[0] <= (byte)'9')
      rank = data[0] - (byte)'0';
    else
    {
      switch(data[0])
      {
        case 'T':
          rank = 10;
        break;
        case 'J':
          rank = 11;
        break;
        case 'Q':
          rank = 12;
        break;
        case 'K':
          rank = 13;
        break;
        case 'A':
          rank = 14;
        break;
      }
    }
    
    suit = Suit.fromByte(data[1]);
  }
  
  public static Comparator<Card> rankComparator = new Comparator<Card>()
  {
    @Override
    public int compare(Card s1, Card s2)
    {
      return s1.getRank() - s2.getRank();
    }
  };
  
  public int compareTo(Card card)
  {
    return rank - card.getRank();
  }
  
  /**
   * @return the rank
   */
  public int getRank() {
    return rank;
  }

  /**
   * @param rank the rank to set
   */
  public void setRank(int rank) {
    this.rank = rank;
  }
  
  /**
   * @return the suit
   */
  public Suit getSuit() {
    return suit;
  }

  /**
   * @param suit the suit to set
   */
  public void setSuit(Suit suit) {
    this.suit = suit;
  }
  
  /**
   * @return the raw
   */
  public String getRaw() {
    return raw;
  }

  /**
   * @param raw the raw to set
   */
  public void setRaw(String raw) {
    this.raw = raw;
  }
}
