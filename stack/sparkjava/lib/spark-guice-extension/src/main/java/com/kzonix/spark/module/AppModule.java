package com.kzonix.spark.module;

import com.google.inject.AbstractModule;
import com.kzonix.spark.ApplicationServiceProvider;
import com.kzonix.spark.conf.ApplicationConfigProvider;
import com.kzonix.spark.starter.ApplicationStarter;
import com.kzonix.spark.starter.SparkApplicationStarter;
import com.typesafe.config.Config;
import spark.Service;

public class AppModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(Config.class).toProvider(ApplicationConfigProvider.class);
    bind(Service.class).toProvider(ApplicationServiceProvider.class);
    bind(ApplicationStarter.class).to(SparkApplicationStarter.class);
  }


}
