package com.clustereddatawarehouse.annotation;

import java.lang.annotation.*;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseWrapper {
    String message() default "Payload Successful";
}
