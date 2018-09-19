package com.kzonix.spark;

import java.util.List;

public interface PageResult<O extends DtoOut> extends DtoOut {

  List<O> getData();

  PageInfoResult getPageInfo();
}
