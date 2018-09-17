package com.kzonix.spark.route;

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
