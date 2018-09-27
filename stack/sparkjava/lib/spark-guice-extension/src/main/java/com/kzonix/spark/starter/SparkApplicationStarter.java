package com.kzonix.spark.starter;

import javax.inject.Inject;
import spark.Service;

public class SparkApplicationStarter implements ApplicationStarter {

  private final Service sparkService;

  @Inject
  public SparkApplicationStarter(Service sparkService) {
    this.sparkService = sparkService;
  }


  @Override
  public void boot() {
    sparkService.init();
  }
}
