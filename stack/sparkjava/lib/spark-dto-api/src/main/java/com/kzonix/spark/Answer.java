package com.kzonix.spark;


public interface Answer<O extends DtoOut> {

  MetaData getMetadata();

  O getBody();

  String getCode();

  AppErrorMap getAppErrorMap();

}
