package com.example;

import org.apache.catalina.core.StandardContext;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
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
        // 配置CharacterEncodingFilter
        FilterDef filterDef = new FilterDef();
        filterDef.setFilterName("CharacterEncodingFilter");
        filterDef.setFilterClass("com.example.CharacterEncodingFilter");
        filterDef.addInitParameter("encoding", "UTF-8");
        ctx.addFilterDef(filterDef);

        FilterMap filterMap = new FilterMap();
        filterMap.setFilterName("CharacterEncodingFilter");
        filterMap.addURLPattern("/*");
        ctx.addFilterMap(filterMap);

        ctx.addApplicationListener("com.example.OnlineUserListener");

        // 添加默认Servlet来处理静态资源
        org.apache.catalina.servlets.DefaultServlet defaultServlet = new org.apache.catalina.servlets.DefaultServlet();
        Tomcat.addServlet(ctx, "default", defaultServlet);
        ctx.addServletMappingDecoded("/", "default");

        // 创建HelloServlet实例
        HelloServlet helloServlet = new HelloServlet();
        ForwardServlet forwardServlet = new ForwardServlet();
        HomeServlet homeServlet = new HomeServlet();

        // 添加HelloServlet到上下文
        Tomcat.addServlet(ctx, "helloServlet", helloServlet);
        Tomcat.addServlet(ctx, "forwardServlet", forwardServlet);
        Tomcat.addServlet(ctx, "homeServlet", homeServlet);

        ctx.addServletMappingDecoded("/hello", "helloServlet");
        ctx.addServletMappingDecoded("/forward", "forwardServlet");
        ctx.addServletMappingDecoded("", "homeServlet");
        // 创建DownloadServlet实例
        DownloadServlet downloadServlet = new DownloadServlet();

        // 添加DownloadServlet到上下文
        Tomcat.addServlet(ctx, "downloadServlet", downloadServlet);
        ctx.addServletMappingDecoded("/download", "downloadServlet");

        // 创建UserServlet实例
        UserServlet userServlet = new UserServlet();
        // 添加UserServlet到上下文
        Tomcat.addServlet(ctx, "userServlet", userServlet);
        ctx.addServletMappingDecoded("/user", "userServlet");

        // 创建User2Servlet实例
        User2Servlet user2Servlet = new User2Servlet();
        // 添加User2Servlet到上下文
        Tomcat.addServlet(ctx, "user2Servlet", user2Servlet);
        ctx.addServletMappingDecoded("/user2", "user2Servlet");
    }
}