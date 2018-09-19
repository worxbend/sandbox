package com.kzonix.spark.route.handler;

import com.kzonix.spark.Answer;
import java.util.Map;
import org.eclipse.jetty.http.HttpHeader;

public interface RequestHandler<I, O> {

  Answer<O> process(I value, Map<String, String> urlParams, Map<HttpHeader, String> headersStringMap);

}
