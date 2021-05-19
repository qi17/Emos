package com.example.emos.wx.exception;

import lombok.Data;

/**
 * @author 齐春晖
 * @date Created in 17:57 2021/5/4
 * @description
 */
@Data
public class EmosException extends RuntimeException{
    private String msg;
    private int code = 500;

    public EmosException(String msg){
        super(msg);
        this.msg =msg;
    }

    public EmosException(String msg,Throwable e){
        super(msg,e);
        this.msg = msg;

    }
    public EmosException(String msg,int code){
        super(msg);
        this.msg = msg;
        this.code= code;

    }
    public EmosException(String msg,int code,Throwable e){
        super(msg,e);
        this.msg = msg;
        this.code = code ;

    }

}
