package com.mage.crm.util;

import com.mage.crm.base.exceptions.ParamsException;

/**
 * 进行值判断的工具类
 */
public class AssertUtil {
    public static void isTrue(Boolean flag,String msg){
        //为true，抛出自定义异常
        if(flag){
            throw new ParamsException(300,msg);
        }
    }
    public static void isTrue(Boolean flag,Integer code,String msg){
        //为true，抛出自定义异常
        if(flag){
            throw new ParamsException(code,msg);
        }
    }

}
