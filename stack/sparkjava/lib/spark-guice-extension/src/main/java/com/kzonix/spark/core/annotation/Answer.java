package com.kzonix.spark.core.annotation;

public interface Answer<T extends DtoOut> {

  int code();

  T body();

  AppErrorMap errorMap();

}
