package com.itheima.reggie.common;

/**
 * @version 1.0
 * @description: 自定义业务异常
 * @date 2022/11/30 9:06
 */
public class CustomException extends RuntimeException{

    public CustomException(String message){
        super(message);
    }

}
