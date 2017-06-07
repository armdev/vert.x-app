package com.vertx.point;

import com.vertx.runner.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

public class PingSender extends AbstractVerticle {
  
  public static void main(String[] args) {
    Runner.runClustered(PingSender.class);
  }

  @Override
  public void start() throws Exception {
    EventBus eb = vertx.eventBus();
    vertx.setPeriodic(1000, v -> {
      eb.send("ping-address", "ping!", reply -> {
        if (reply.succeeded()) {
          System.out.println("Received reply::: " + reply.result().body());
        } else {
          System.out.println("No reply?");
        }
      });
    });
  }
}
