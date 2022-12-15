package com.secomind.poc.aggregator.aggregator;

public class Report {
  private int success;
  private int errors;

  public Report() {
    this.success = 0;
    this.errors = 0;
  }

  public int getSuccess() {
    return success;
  }

  public int getErrors() {
    return errors;
  }

  public void incrementSuccess() {
    this.success++;
  }

  public void incrementError() {
    this.errors++;
  }

  @Override
  public String toString() {
    return "Report{" + "success=" + success + ", errors=" + errors + '}';
  }
}
