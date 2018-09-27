package com.kzonix.spark.route.handler;

import com.kzonix.spark.Answer;
import com.kzonix.spark.DtoIn;
import com.kzonix.spark.DtoOut;
import java.util.Map;
import org.eclipse.jetty.http.HttpHeader;

public interface RequestHandler<I extends DtoIn, O extends DtoOut> {

  Answer<O> process(I value, Map<String, String> urlParams, Map<HttpHeader, String> headersStringMap);

}
