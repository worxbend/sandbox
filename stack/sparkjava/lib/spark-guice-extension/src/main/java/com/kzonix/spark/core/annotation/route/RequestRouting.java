package com.kzonix.spark.core.annotation.route;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import spark.route.HttpMethod;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Routing
public @interface RequestRouting {

  String name() default "";

  String[] value() default {};

  String[] path() default {};

  HttpMethod[] method() default {};

  String[] params() default {};

  String[] produces() default {};

  String[] consumes() default {};

}
