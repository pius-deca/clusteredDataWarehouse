package com.clustereddatawarehouse.aop.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericResponse<T> {
    public static final String SUCCESS_KEY = "Success";
    public static final String FAILED_KEY = "Failed";

    private String status;
    private String message;
    private T data;

}
