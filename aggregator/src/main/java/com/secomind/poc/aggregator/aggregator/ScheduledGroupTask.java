package com.secomind.poc.aggregator.aggregator;

import java.util.TimerTask;

public class ScheduledGroupTask extends TimerTask {
  @Override
  public void run() {
    Aggregator aggregator = Aggregator.getInstance();
    // TODO: Update Astarte Java SDK to support Arrays
    // aggregator.sendGroups();
    System.out.println("Here we should publish data from groups");
  }
}
