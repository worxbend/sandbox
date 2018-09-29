package com.kzonix.spark.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kzonix.spark.Answer;
import com.kzonix.spark.DtoIn;
import com.kzonix.spark.DtoOut;
import com.kzonix.spark.route.AbstractRoute;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import org.eclipse.jetty.http.HttpHeader;
import spark.Request;
import spark.Response;

public abstract class RestRequestHandler<I extends DtoIn, O extends DtoOut> extends AbstractRoute<I, O> {

  private static ObjectMapper mapper = Json.mapper();

  protected RestRequestHandler() {

  }

  @Override
  public Answer<O> process(I value, Map<String, String> urlParams, Map<HttpHeader, String> headersStringMap) {
    return null;
  }

  @Override
  public Object handle(Request request, Response response) {
    ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
    final Class<I> dtoInType = (Class<I>) parameterizedType.getActualTypeArguments()[0];
    final Type type = parameterizedType.getActualTypeArguments()[0];
    final Class<O> dtoOutType = (Class<O>) parameterizedType.getActualTypeArguments()[1];
    I dtoIn = parseDtoIn(request, dtoInType);
    //TODO: implement hocon-validator, and design architecture for validation-matchers

    return null;
  }

  private I parseDtoIn(Request request, Class<I> dtoInType) {
    try {
      return mapper.readValue(request.body(), dtoInType);
    } catch (IOException e) {
      throw new RuntimeException();
    }
  }
}
