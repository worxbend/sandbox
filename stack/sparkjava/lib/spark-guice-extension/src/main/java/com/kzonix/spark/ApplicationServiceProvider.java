package com.kzonix.spark;

import com.google.inject.Provider;
import spark.Service;

public class ApplicationServiceProvider implements Provider<Service>{

  @Override
  public Service get() {
    return SingletonHolder.service;
  }

  private static final class SingletonHolder {
    private static final Service service = Service.ignite();
  }
}
