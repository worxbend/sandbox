package com.kzonix.spark.rest.module;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.kzonix.spark.rest.Json;

public class JacksonGuiceModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(ObjectMapper.class).toInstance(Json.mapper());
  }
}
