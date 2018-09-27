package com.kzonix.spark.route.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import spark.route.HttpMethod;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Filter {

  String name() default "";

  int order() default 0;

  String[] value() default {};

  String[] path() default {};

  HttpMethod[] method() default {};

}
