package com.netposa.vetl.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @program: np-police-event
 * @description:
 * @author: kangyu
 * @create: 2019-12-19 11:04
 **/
@Data
@AllArgsConstructor
public class WebResult {

    private int code;

    private Object data;

    private String message;

    public final static int SUCCESS_CODE = 200;

    public final static int ERROR_CODE = 500;

    public final static int OUTDATE_CODE = 402;

    public final static String OUTDATE_MESSAGE = "用户已过期，请重新登陆";

    public final static String SUCCESS_MESSAGE = "成功";

    public static WebResult SUCCESS(Object data){
        return new WebResult(SUCCESS_CODE,data,SUCCESS_MESSAGE);
    }

    public static WebResult ERROR(String message){
        return new WebResult(ERROR_CODE,null,message);
    }

}
