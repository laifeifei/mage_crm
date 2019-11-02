package com.mage.crm.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 获取cookie的工具类
 */
public class CookieUtil {
    public static  String  getCookieValue(HttpServletRequest request,String key){
        Cookie[] cookies = request.getCookies();
        if (cookies!=null) {   //防止空指针异常
            for (Cookie cookie:cookies){
                if (key.equals(cookie.getName())){
                    try {
                        return URLDecoder.decode(cookie.getValue(),"utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}
