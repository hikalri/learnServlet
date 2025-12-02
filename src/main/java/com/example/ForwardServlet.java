package com.example;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 转发Servlet示例
 * 该Servlet接收请求并将其转发到HelloServlet
 */
public class ForwardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 设置请求编码为UTF-8
        request.setCharacterEncoding("UTF-8");
        Cookie[] cookie = request.getCookies();
        // 使用sendRedirect重定向到HelloServlet
        // 注意：sendRedirect会在客户端发起新的请求，原请求中的属性会丢失
        response.sendRedirect("/hello");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 设置请求编码为UTF-8
        request.setCharacterEncoding("UTF-8");

        // 使用sendRedirect重定向到HelloServlet
        // 注意：sendRedirect会在客户端发起新的请求，POST请求会变成GET请求
        response.sendRedirect("/hello");
    }
}