package com.kzonix.spark.core.annotation.route;

public abstract class AbstractRoute implements SparkRoute {

  private String basePath = "/";


  protected AbstractRoute(String basePath) {
    this.basePath = basePath;
  }

  protected AbstractRoute() {
  }

  @Override
  public String getBaseRoutePath() {
    return basePath;
  }

  public void setBaseRoutePath(String baseRoutePath) {
    this.basePath = baseRoutePath;
  }
}
