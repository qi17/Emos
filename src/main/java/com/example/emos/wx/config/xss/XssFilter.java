package com.example.emos.wx.config.xss;

import java.io.IOException;
import java.util.logging.LogRecord;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

/**
 * @author 齐春晖
 * @date Created in 18:07 2021/5/5
 * @description
 */
@WebFilter(urlPatterns = "/*")  //过滤所有请求路径
public class XssFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        XssHttpServletRequestWrapper servletRequestWrapper = new XssHttpServletRequestWrapper(
            (HttpServletRequest) servletRequest);
        filterChain.doFilter(servletRequestWrapper,servletResponse);

    }

    @Override
    public void destroy() {

    }
}
