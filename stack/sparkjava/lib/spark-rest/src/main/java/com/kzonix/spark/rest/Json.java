package com.kzonix.spark.rest;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr353.JSR353Module;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.fasterxml.jackson.module.paranamer.ParanamerModule;

public class Json {

  private static ObjectMapper objectMapper = SingletonHolder.objectMapper;

  private Json() {
  }

  public static ObjectMapper mapper() {
    return objectMapper;
  }

  private static class SingletonHolder {

    private static ObjectMapper objectMapper = getObjectMapper();

    private static ObjectMapper getObjectMapper() {
      final ObjectMapper mapper = new ObjectMapper();

      // Don't throw an exception when json has extra fields you are
      // not serializing on. This is useful when you want to use a pojo
      // for deserialization and only care about a portion of the json
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

      // Ignore null values when writing json.
      mapper.setSerializationInclusion(Include.ALWAYS);

      // Write times as a String instead of a Long so its human readable.
      mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
      mapper.registerModule(new AfterburnerModule());
      mapper.registerModule(new SimpleModule());
      mapper.registerModule(new Jdk8Module());
      mapper.registerModule(new JavaTimeModule());
      mapper.registerModule(new JSR353Module());
      mapper.registerModule(new ParameterNamesModule());
      mapper.registerModule(new ParanamerModule());
      mapper.registerModule(new GuavaModule());
      mapper.registerModule(new Hibernate5Module());
      return mapper;
    }
  }
}
