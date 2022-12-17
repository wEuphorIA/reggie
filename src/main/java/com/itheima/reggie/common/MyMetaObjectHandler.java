package com.itheima.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @version 1.0
 * @description: 自定义源数据处理器
 * @date 2022/11/29 23:11
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    
    /** 
     * @Description: 插入操作自动填充 
     * @param: metaObject 
     * @return: void
     * @date: 2022/11/29 23:19
     */ 
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充[insert]...");
        log.info(metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", BaseContext.getCurrentId());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }
    /** 
     * @Description: 更新操作自动填充
     * @param: metaObject 
     * @return: void
     * @date: 2022/11/29 23:19
     */ 
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充[update]...");
        log.info(metaObject.toString());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }
}
