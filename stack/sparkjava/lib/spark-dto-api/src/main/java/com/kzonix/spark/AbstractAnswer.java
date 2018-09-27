package com.kzonix.spark;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractAnswer<O extends DtoOut> implements Answer<O> {

  private O body;
  private MetaData metaData;
  private int code;
  private AppErrorMap appErrorMap;

}
