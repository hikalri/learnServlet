package com.example;

import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

/**
 * Tomcat配置类，用于配置Servlet
 */
public class TomcatConfig {

    /**
     * 配置Servlet
     * @param ctx Tomcat上下文
     */
    public static void configureServlets(StandardContext ctx) {
        // 创建HelloServlet实例
        HelloServlet helloServlet = new HelloServlet();
        ForwardServlet forwardServlet = new ForwardServlet();
        // 添加HelloServlet到上下文
        Tomcat.addServlet(ctx, "helloServlet", helloServlet);
        Tomcat.addServlet(ctx, "forwardServlet", forwardServlet);
        ctx.addServletMappingDecoded("/hello", "helloServlet");
        ctx.addServletMappingDecoded("/forward", "forwardServlet");
        // 创建DownloadServlet实例
        DownloadServlet downloadServlet = new DownloadServlet();

        // 添加DownloadServlet到上下文
        Tomcat.addServlet(ctx, "downloadServlet", downloadServlet);
        ctx.addServletMappingDecoded("/download", "downloadServlet");
    }
}