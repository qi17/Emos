package com.example.emos.wx.aop;

import com.example.emos.wx.common.util.R;
import com.example.emos.wx.config.shrio.ThreadLocalToken;
import com.sun.org.apache.xpath.internal.objects.XObject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 齐春晖
 * @date Created in 16:23 2021/5/16
 * @description
 */
@Aspect
@Component
public class tokenAop {
    @Autowired
    private ThreadLocalToken localToken;

    @Pointcut("execution(public * com.example.emos.wx.controller.*.*(..)))")
    public void aspect(){

    }
    @Around("aspect()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws  Throwable{

       R r  = (R) proceedingJoinPoint.proceed();
        String token = localToken.getToken();
        if(token != null){
            r.put("token",token);
            localToken.clear();
        }
        return r;
    }
}
