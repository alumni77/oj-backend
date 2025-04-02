package com.zjedu.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author Zhong
 * @Create 2025/4/2 19:55
 * @Version 1.0
 * @Description
 */

@Component
public class MyMetaObjectHandler implements MetaObjectHandler
{

    @Override
    public void insertFill(MetaObject metaObject)
    {
        this.strictInsertFill(metaObject, "gmtCreate", Date.class, new Date());
        this.strictInsertFill(metaObject, "gmtModified", Date.class, new Date());
    }

    @Override
    public void updateFill(MetaObject metaObject)
    {
        this.strictUpdateFill(metaObject, "gmtModified", Date.class, new Date());
    }
}