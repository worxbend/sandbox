package com.kzonix.spark;


public interface Answer<O> {

  MetaData getMetadata();

  O getBody();

  int code();

  AppErrorMap getAppErrorMap();

}
