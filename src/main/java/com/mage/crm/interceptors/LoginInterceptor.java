package com.mage.crm.interceptors;

import com.mage.crm.base.CrmConstant;
import com.mage.crm.service.UserService;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.util.Base64Util;
import com.mage.crm.util.CookieUtil;
import com.mage.crm.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截，拦截未登录时跳转到主页
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取浏览器中的cookie
        String userId = CookieUtil.getCookieValue(request, "userId");
        //userId为空，用户未登录
        AssertUtil.isTrue(StringUtils.isBlank(userId), CrmConstant.LOGIN_NOLOGIN_CODE,CrmConstant.LOGIN_NOLOGIN_MSG);
        //将userId进行解密
        String id = Base64Util.deCode(userId);
        //通过Service层查询用户是否存在
        User user = userService.queryUserById(id);
        //用户为空，不存在
        AssertUtil.isTrue(null==user, CrmConstant.LOGIN_NOLOGIN_CODE,CrmConstant.LOGIN_NOLOGIN_MSG);
        //判断用户是否已经注销
        AssertUtil.isTrue("0".equals(user.getIsValid()), CrmConstant.LOGIN_NOLOGIN_CODE,CrmConstant.LOGIN_NOLOGIN_MSG);
        return true;
    }
}
