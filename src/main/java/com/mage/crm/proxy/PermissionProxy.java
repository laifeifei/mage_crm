package com.mage.crm.proxy;

import com.mage.crm.RequestPermission;
import com.mage.crm.util.AssertUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 设置切面，对非法请求进行拦截
 */
@Component
@Aspect
public class PermissionProxy {
    @Resource
    private HttpSession session;

    @Pointcut("@annotation(com.mage.crm.RequestPermission)")
    public void cut(){}

    /**
     * 方法前进行拦截
     * @param jp
     * @throws Throwable
     */
    @Before(value = "cut()")
    public void before(JoinPoint jp) throws Throwable{
        MethodSignature methodSignature= (MethodSignature) jp.getSignature();
        Method method= methodSignature.getMethod();
        RequestPermission requestPermission= method.getAnnotation(RequestPermission.class);
        if(null !=requestPermission){
            //System.out.println("权限值:"+requestPermission.aclVal());
            List<String> permissions= (List<String>) session.getAttribute("userPermission");
            AssertUtil.isTrue(CollectionUtils.isEmpty(permissions),"暂无权限!");
            AssertUtil.isTrue(!(permissions.contains(requestPermission.aclVal())),"暂无权限!");
        }
    }
}
