package com.kzonix.spark.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kzonix.spark.Answer;
import com.kzonix.spark.DtoIn;
import com.kzonix.spark.DtoOut;
import com.kzonix.spark.route.AbstractRoute;
import java.util.Map;
import javax.inject.Inject;
import org.eclipse.jetty.http.HttpHeader;
import spark.Request;
import spark.Response;

public abstract class RestRequestHandler<I extends DtoIn, O extends DtoOut> extends AbstractRoute<I, O> {

  @Inject
  private ObjectMapper objectMapper;

  @Override
  public Answer<O> process(I value, Map<String, String> urlParams, Map<HttpHeader, String> headersStringMap) {
    return null;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    return null;
  }
}
