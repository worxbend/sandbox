package com.kzonix.spark.route;

import com.google.common.collect.Sets;
import com.kzonix.spark.route.handler.RequestHandler;
import java.util.Collections;
import java.util.Set;

public class HandlerRegistry {

  private static final Set<RequestHandler> handlers = SingletonHolder.handlers;

  private HandlerRegistry() {
  }

  public static Set<RequestHandler> handlers() {
    return Collections.unmodifiableSet(handlers);
  }

  public static void register(RequestHandler requestHandler) {
    handlers.add(requestHandler);
  }

  private static class SingletonHolder {

    private static final Set<RequestHandler> handlers = Sets.newConcurrentHashSet();
  }
}
