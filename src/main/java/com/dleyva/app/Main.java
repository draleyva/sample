package com.dleyva.app;

import ch.qos.logback.classic.Logger;
import com.sft.app.util.AppDefinition;

/**
 *
 * @author David Ricardo
 */
public class Main 
{  
  public static Logger LOGGER = (Logger)org.slf4j.LoggerFactory.getLogger(Main.class);
  
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args)
  {
    try
    {
      new SampleApp("Sample").app(args);
    }
    catch(Exception e)
    {
      LOGGER.error("main->", e);
      System.exit(AppDefinition.EXCEPTION);
    }
  }
}
