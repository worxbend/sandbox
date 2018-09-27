package com.kzonix.spark.route;

import com.kzonix.spark.DtoIn;
import com.kzonix.spark.DtoOut;
import com.kzonix.spark.route.handler.RequestHandler;

public abstract class AbstractRoute<I extends DtoIn, O extends DtoOut> implements SparkRoute, RequestHandler<I, O> {

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
