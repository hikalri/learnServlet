package com.example;

import javax.servlet.*;
import java.io.IOException;

/**
 * 字符编码过滤器示例
 * 统一设置请求和响应的字符编码为 UTF-8
 */
public class CharacterEncodingFilter implements Filter {

    private String encoding = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String param = filterConfig.getInitParameter("encoding");
        if (param != null && !param.trim().isEmpty()) {
            encoding = param.trim();
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // 设置请求字符编码
        request.setCharacterEncoding(encoding);
        // 设置响应字符编码
        response.setCharacterEncoding(encoding);

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // nothing to do
    }
}