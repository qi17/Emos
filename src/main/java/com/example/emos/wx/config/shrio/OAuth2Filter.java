package com.example.emos.wx.config.shrio;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import java.io.IOException;
import java.sql.Struct;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpStatus;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import sun.awt.geom.AreaOp.CAGOp;

/**
 * @author 齐春晖
 * @date Created in 21:06 2021/5/12
 * @description
 */
@Component
@Scope("prototype")//在spring的IOC容器中使用多例模式创建实例
public class OAuth2Filter extends AuthenticatingFilter {

    @Autowired
    private RedisTemplate redisTemplate;//将token保存到redis

    @Autowired
    private ThreadLocalToken threadLocalToken;//保存token的类

    @Value("${emos.jwt.cache-expire}")
    private int expireDate;//令牌过期时间

    @Autowired
    private JWTUtil jwtUtil; //用于令牌加密的工具类

    /**
     * 生成token
     *
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest,
                                              ServletResponse servletResponse) throws Exception {

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        //从请求中获取token并进行判断
        String tokenStr = getTokenByRequest(request);
        if (StrUtil.isBlank(tokenStr)) {
            return null;
        }

        return new OAuth2Token(tokenStr);
    }

    /**
     * 是否允许过滤
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response,
                                      Object mappedValue) {
//        判断是否是Option请求
        HttpServletRequest req = (HttpServletRequest) request;
        if (req.getMethod()
            .equals(HttpMethod.OPTIONS.name())) {
            return true;//放过OPTIONS请求
        }
        return false;
    }

    /**
     * 处理所有应该有shiro处理的请求 也就是不被放过的请求
     *
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse)
        throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Content-Type", "text/html;charset=UTF-8");
        //允许跨域请求
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", response.getHeader("Origin"));
        //清空当前ThreadLocalToken中的token，因为我们后面可能要新生成token
        threadLocalToken.clear();
        //从请求中获取令牌
        String tokenStr = getTokenByRequest(request);
        //如果令牌为空，在响应中返回前端相关信息
        if (StrUtil.isBlank(tokenStr)) {
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            response.getWriter()
                .print("no useful token");
            return false;
        }

        //如果令牌存在，刷新令牌
        try {
            //校验令牌
            jwtUtil.verifierToken(tokenStr);
        } catch (TokenExpiredException e) { //发现令牌过期

            //去redis中找
            if (redisTemplate.hasKey("token")) {
                //redis存在，重新为客户生成一个新的token
                redisTemplate.delete("token");
                int userId = jwtUtil.getUserId(tokenStr);
                String token = jwtUtil.createToken(userId);
                redisTemplate.opsForValue()
                    .set(token, userId + "", expireDate, TimeUnit.DAYS);
                threadLocalToken.setToken(token);

            }
            //redis令牌未过期,需要重新登录
            else {
                response.setStatus(HttpStatus.SC_UNAUTHORIZED);
                response.getWriter()
                    .print("令牌已经过期");
                return false;
            }

        } catch (JWTDecodeException e) {
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            response.getWriter()
                .print("无效的令牌");
            return false;
        }
        boolean bool = executeLogin(request, response);
        return bool;

    }

    /**
     * 处理登录失败的情况
     *
     * @param token
     * @param e
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token,
                                     AuthenticationException e, ServletRequest request,
                                     ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.setStatus(HttpStatus.SC_UNAUTHORIZED);
        resp.setContentType("application/json;charset=utf-8");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        try {
            resp.getWriter()
                .print(e.getMessage());
        } catch (IOException exception) {
        }
        return false;

    }

    private String getTokenByRequest(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("token");

        if (StrUtil.isBlank(token)) {
            //如果请求头中不存在就去请求体中获取
            token = httpServletRequest.getParameter("token");
        }

        return token;
    }
}
