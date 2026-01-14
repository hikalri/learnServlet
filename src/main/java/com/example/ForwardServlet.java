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
        Cookie[] cookie = request.getCookies();
        // 如果请求中没有cookie，则在响应中设置一个cookie
        boolean hasCookie = (cookie != null && cookie.length > 0);
        if (!hasCookie) {
            Cookie newCookie = new Cookie("forwardCookie", String.valueOf(System.currentTimeMillis()));
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24); // 1天
            response.addCookie(newCookie);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 设置请求编码为UTF-8
        request.setCharacterEncoding("UTF-8");

        Cookie[] cookie = request.getCookies();
        // 如果请求中没有cookie，则在响应中设置一个cookie
        boolean hasCookie = (cookie != null && cookie.length > 0);
        if (!hasCookie) {
            Cookie newCookie = new Cookie("forwardCookie", String.valueOf(System.currentTimeMillis()));
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24); // 1天
            response.addCookie(newCookie);
        }
    }
}