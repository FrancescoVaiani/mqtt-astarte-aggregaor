package com.secomind.poc.aggregator.aggregator;

import java.util.TimerTask;

public class ScheduledReportTask extends TimerTask {
  @Override
  public void run() {
    Aggregator aggregator = Aggregator.getInstance();
    aggregator.sendReports();
  }
}
