package com.example.emos.wx.config.shrio;

import org.springframework.stereotype.Component;

/**
 * @author 齐春晖
 * @date Created in 23:11 2021/5/11
 * @description
 */
@Component
public class ThreadLocalToken {
    private  ThreadLocal<String> threadLocal;

    public void setToken(String token){
        threadLocal.set(token);
    }

    public String getToken(){

      return   threadLocal.get();
    }

    public void clear(){
         threadLocal.remove();
    }

}
