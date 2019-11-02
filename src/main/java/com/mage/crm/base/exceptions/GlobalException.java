package com.mage.crm.base.exceptions;

import com.alibaba.fastjson.JSON;
import com.mage.crm.base.CrmConstant;
import com.mage.crm.model.MessageModel;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * 全局异常处理
 */
@Component
public class GlobalException implements HandlerExceptionResolver {
    /**
     * 1.判断是否未登录
     * 2.json异常
     * 3.视图异常
     * @param httpServletRequest
     * @param httpServletResponse
     * @param handler
     * @param e
     * @return
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, Exception e) {
        //声明ModelAndView对象
        ModelAndView modelAndView = createDefaultModelAndView(httpServletRequest);
        ParamsException paramsException;
        if (handler instanceof HandlerMethod) {
            //判断是否是未登录异常
            if (e instanceof ParamsException) {
                //将异常强转为自定义异常
                paramsException = (ParamsException) e;
                //判断状态码是否是未登录异常
                if (paramsException.getCode() == CrmConstant.LOGIN_NOLOGIN_CODE) {
                    //设置参数
                    modelAndView.addObject("code", paramsException.getCode());
                    modelAndView.addObject("msg", paramsException.getMsg());
                    return modelAndView;
                }
            }
            //json异常
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            //获取ResponseBody注解
            ResponseBody responseBody = method.getAnnotation(ResponseBody.class);
            //存在ResponseBody注解，则是json异常
            if (null != responseBody) {
                //创建MessageModel对象
                MessageModel messageModel = new MessageModel();
                //设置参数
                messageModel.setCode(CrmConstant.OPS_FAILED_CODE);
                messageModel.setMsg(CrmConstant.OPS_FAILED_MSG);
                //判断是否是自定义异常
                if (e instanceof ParamsException) {
                    //强制类型转换
                    paramsException = (ParamsException) e;
                    //设置参数
                    messageModel.setCode(paramsException.getCode());
                    messageModel.setMsg(paramsException.getMsg());
                }
                //设置编码格式
                httpServletResponse.setContentType("application/json;charset=uft-8");
                httpServletResponse.setCharacterEncoding("utf-8");
                PrintWriter printWriter = null;
                try {
                    printWriter = httpServletResponse.getWriter();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    if (printWriter != null) {
                        printWriter.write(JSON.toJSONString(messageModel));
                        printWriter.flush();
                        printWriter.close();
                    }
                }
                return null;
            }
        } else {
            //视图异常
            if (e instanceof ParamsException) {
                paramsException = (ParamsException) e;
                modelAndView.addObject("code", paramsException.getCode());
                modelAndView.addObject("msg", paramsException.getMsg());
                return modelAndView;
            } else {
                return modelAndView;
            }
        }
        return null;
    }

    /**
     * 将相关的属性值设置在modelAndView中
     * @param request
     * @return
     */
    public static ModelAndView createDefaultModelAndView(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.addObject("code", CrmConstant.OPS_FAILED_CODE);
        modelAndView.addObject("msg",CrmConstant.OPS_FAILED_MSG);
        //防止拦截器没有放行，无法获取到ctx
        modelAndView.addObject("ctx",request.getContextPath());
        modelAndView.addObject("uri",request.getRequestURI());
        return modelAndView;

    }
}
