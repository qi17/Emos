package com.example.emos.wx.common.util;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;

/**
 * @author 齐春晖
 * @date Created in 22:43 2021/5/4
 * @description
 */
public class R extends HashMap<String,Object> {

    private String msg;
    private int code;

    public R(){
        put("code", HttpStatus.OK.value());
        put("msg","success");

    }
    public R put(String key,Object value){
        super.put(key,value);
        return this;
    }

    public static R ok(){
        return  new R();
    }
    public static R ok(String msg){
        R  r =new R();
        r.put("message",msg);
        return  r;
    }
    public static  R ok(Map<String,Object> map){
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R error(){
        return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "未知错误，请与管理员联系");
    }

    public static R error(String msg){
        R r = new R();
        r.put("msg",msg);
        return  r;

    }
    public static R error(int code,String msg){
        R r = new R();
        r.put("code",code);
        r.put("msg",msg);
        return r;
    }

}
