package com.vertx.pubsub;

import com.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

public class SecondSender extends AbstractVerticle {

 
  public static void main(String[] args) {      
    Runner.runClustered(SecondSender.class);
  }

  @Override
  public void start() throws Exception {
    EventBus eb = vertx.eventBus();  
    vertx.setPeriodic(1000, v -> eb.publish("news-feed", "Political News"));
  }
}
