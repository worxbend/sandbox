package com.kzonix.spark.route;

import spark.Filter;

/**
 * Abstract declaration of
 **/
public abstract class AbstractFilter implements Filter {

  private long priority = 0;

  public AbstractFilter(long filterPriority) {
    this.priority = filterPriority;
  }

  public long getPriority() {
    return priority;
  }

  public void setPriority(long priority) {
    this.priority = priority;
  }
}
