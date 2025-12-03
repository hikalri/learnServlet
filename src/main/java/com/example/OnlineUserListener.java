package com.example;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * 监听器，用于统计在线人数
 */
public class OnlineUserListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        ServletContext ctx = se.getSession().getServletContext();

        // 使用Context对象锁保证线程安全
        synchronized (ctx) {
            Integer onlineCount = (Integer) ctx.getAttribute("onlineCount");
            if (onlineCount == null) {
                onlineCount = 0;
            }
            onlineCount++;
            ctx.setAttribute("onlineCount", onlineCount);
            System.out.println("Session created. Online users: " + onlineCount);
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        ServletContext ctx = se.getSession().getServletContext();

        synchronized (ctx) {
            Integer onlineCount = (Integer) ctx.getAttribute("onlineCount");
            if (onlineCount == null) {
                onlineCount = 0;
            } else {
                if (onlineCount > 0) {
                    onlineCount--;
                }
            }
            ctx.setAttribute("onlineCount", onlineCount);
            System.out.println("Session destroyed. Online users: " + onlineCount);
        }
    }
}