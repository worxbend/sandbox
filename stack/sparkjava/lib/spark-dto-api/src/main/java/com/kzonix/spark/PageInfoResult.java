package com.kzonix.spark;

public interface PageInfoResult<T> extends PageInfo {

  long getTotalCount();

  T[] getContent();

  T[] getContent(int count);
}
