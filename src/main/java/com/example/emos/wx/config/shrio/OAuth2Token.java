package com.example.emos.wx.config.shrio;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author 齐春晖
 * @date Created in 19:21 2021/5/5
 * @description
 *  封装Token/令牌的对象，继承于shiro的AuthenticationToken
 */

public class OAuth2Token implements AuthenticationToken {

    private String token;

    /**
     * 创建生成token对象的构造器
     *
     * @param token
     */
    public OAuth2Token(String token){
        this.token = token;
    }
    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
