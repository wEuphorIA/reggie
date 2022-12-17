package com.itheima.reggie.common;

/**
 * @version 1.0
 * @description: 基于ThreadLocal封装的一个工具类，用于保存和获取当前用户的id
 * @date 2022/11/29 23:39
 */
public class BaseContext {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * @Description: 设置值
     * @param: id
     * @return: void
     * @date: 2022/11/29 23:47
     */
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    /**
     * @Description: 返回值
     * @param:
     * @return: java.lang.Long
     * @date: 2022/11/29 23:47
     */
    public static Long getCurrentId( ){
        return threadLocal.get();
    }
}
