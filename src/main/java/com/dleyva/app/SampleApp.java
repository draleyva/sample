/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dleyva.app;

import com.google.common.base.Splitter;
import com.google.gson.Gson;
import com.sft.app.util.App;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import picocli.CommandLine;

/**
 *
 * @author David Ricardo
 */

@CommandLine.Command(description = "Sample", name = "Sample", mixinStandardHelpOptions = true, version = "Sample 1.0")
public class SampleApp extends App implements Callable<Void>
{
  static Splitter CARDSPLITTER = Splitter.on(" ").trimResults().limit(10);
    
  @CommandLine.Option(names = { "-p", "--pokerdata" }, paramLabel = "pokerdata", description = "Poker Data")
  String pokerData;
  
  protected Map<String, String> options = new HashMap<>();
  
  public SampleApp(String name) 
  {
    super(name);
  }

  @Override
  public void app(String[] args) throws Exception 
  {
    new CommandLine(this).execute(args);
  }
  
  @Override
  public Void call() throws Exception 
  { 
    LOGGER.info("Sample");
    
    File file = new File(pokerData); 
  
    BufferedReader br = new BufferedReader(new FileReader(file)); 
  
    String rawplay;
    List<String> cardlist;
    List<Play> plays = new ArrayList<>();
    Play play;

    while ((rawplay = br.readLine()) != null)
    {
      cardlist = CARDSPLITTER.splitToList(rawplay);
      play = new Play(cardlist);
      
      LOGGER.debug("evaluate : {}", play.evaluate());
      
      plays.add(play);
    }
    
    int winner;
    
    LOGGER.debug("test : {}", Hand.PlayRank.RoyalFlush.compareTo(Hand.PlayRank.TwoPairs));
    
    int [] counter = new int[3];
      
    for(Play p : plays)
    {
      winner = p.evaluate();
      
      ++counter[winner];
      
      LOGGER.info("play : {}", p.show());
    }
    
    LOGGER.info("1: {}", counter[1]);
    LOGGER.info("2: {}", counter[2]);
    LOGGER.info("3: {}", counter[0]);
    LOGGER.info("4:");
    LOGGER.info("--------PLAYER 1--------|--------PLAYER 2--------");
    
    return null;
  }
}
