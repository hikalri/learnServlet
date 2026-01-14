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
        // 可以读取 web.xml 或注解中配置的初始化参数
        // 当然目前项目里没有 web.xml 和 注解，这里只是为了演示
        String param = filterConfig.getInitParameter("encoding");
        if (param != null && !param.trim().isEmpty()) {
            encoding = param.trim();
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // 如何解析 POST 请求体中的参数
        request.setCharacterEncoding(encoding);
        // 返回给浏览器的内容编码，告诉 response.getWriter() 使用什么字符集将 Java 的字符转化为字节
        response.setCharacterEncoding(encoding);
        // 将控制权移交给链中的下一个过滤器，如果已经是最后一个，则交给目标资源（如 Controller 或 Servlet）
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // nothing to do
    }
}