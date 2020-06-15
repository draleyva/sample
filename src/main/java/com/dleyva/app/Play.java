/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dleyva.app;

import com.dleyva.app.Hand.Evaluation;
import java.util.List;

/**
 *
 * @author David Ricardo
 */
public class Play
{
  Hand player1;
  Hand player2;
  
  public Play(List<String> cardlist)
  {
    int rawcard;
    
    player1 = new Hand();
    player2 = new Hand();
    
    for(rawcard = 0; rawcard < 5; ++rawcard)
    {
      player1.addCard(new Card(cardlist.get(rawcard)));
      player2.addCard(new Card(cardlist.get(5+rawcard)));
    }
  }
  
  public int evaluate()
  {
    Evaluation player1evaluation = player1.evaluate();
    Evaluation player2evaluation = player2.evaluate();
    
    int evaluation = player1evaluation.getPlayRank().compareTo(player2evaluation.getPlayRank());
    
    if(evaluation == 0)
    {
      evaluation = player1evaluation.getCard().compareTo(player2evaluation.getCard());
        
      if(evaluation == 0)
      {
        evaluation = player1.getReverseTopCard().compareTo(player2.getReverseTopCard());
        
        if(evaluation == 0)
          return 0;
      }     
    }
    
    return evaluation < 0? 2:1;
  }
  
  public String show()
  {
    return player1.show() + " " + player2.show();
  }
}
