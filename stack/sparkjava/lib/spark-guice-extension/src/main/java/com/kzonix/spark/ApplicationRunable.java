package com.kzonix.spark;

public interface ApplicationRunable {

  void run(Class<?> applicationClass, String[] args);
}
