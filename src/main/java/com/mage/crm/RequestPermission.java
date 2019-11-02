package com.mage.crm;

import java.lang.annotation.*;

/**
 * 自定义注解
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestPermission {
    //设置参数
    String aclVal();
}
