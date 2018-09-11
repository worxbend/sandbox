package com.kzonix.spark.core.web;

import java.util.Map;

public interface RequestHadler<V extends DtoIn> {

  Answer process(V value, Map<String, String> urlParams);

}
