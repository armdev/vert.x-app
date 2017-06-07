package com.vertx.point;

import com.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

public class PingReceiver extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runClustered(PingReceiver.class);
  }
  
  @Override
  public void start() throws Exception {
    EventBus eb = vertx.eventBus();
    eb.consumer("ping-address", message -> {
      System.out.println("Received message: " + message.body());
      // Now send back reply
      message.reply("JustPong!!!!");
    });
    System.out.println("Receiver ready!");
  }
}
