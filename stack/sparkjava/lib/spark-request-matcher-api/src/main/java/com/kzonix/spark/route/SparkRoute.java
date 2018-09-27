package com.kzonix.spark.route;

import spark.Route;

public interface SparkRoute extends Route {

  String getBaseRoutePath();
}
