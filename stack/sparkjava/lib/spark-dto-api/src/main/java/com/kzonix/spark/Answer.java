package com.kzonix.spark;


public interface Answer<O extends DtoOut> {

  MetaData getMetadata();

  O getBody();

  int code();

  AppErrorMap getAppErrorMap();

}
