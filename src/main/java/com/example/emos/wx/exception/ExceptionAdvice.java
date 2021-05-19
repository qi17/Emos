package com.example.emos.wx.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author 齐春晖
 * @date Created in 16:44 2021/5/16
 * @description
 */
@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {
    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private String exceptionHandler(Exception e){
            log.error("执行异常");
            if(e instanceof MethodArgumentNotValidException){
                MethodArgumentNotValidException exception =(MethodArgumentNotValidException)  e;
                return  exception.getBindingResult().getFieldError().getDefaultMessage();
            }
            else if (e instanceof  EmosException){
                EmosException exception = (EmosException) e;
                return  exception.getMessage();
             }
            else if(e instanceof UnauthenticatedException){
                return "你不具备相关权限";

            }else {
                return "后端执行异常！";
            }


    }

}
