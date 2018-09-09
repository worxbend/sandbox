package com.kzonix.spark.core.annotation;

import java.util.Map;

public interface RequestHadler<V extends Validated> {

  Answer process(V value, Map<String, String> urlParams);

}
