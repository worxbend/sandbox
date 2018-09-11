package com.kzonix.spark.core.web;

public interface Answer<T extends DtoOut> {

  int code();

  T body();

  AppErrorMap errorMap();

}
