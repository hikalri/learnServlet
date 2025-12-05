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
        Tomcat.addServlet(ctx, "default", new org.apache.catalina.servlets.DefaultServlet());
        ctx.addServletMappingDecoded("/", "default");

        // 添加所有Servlet
        Tomcat.addServlet(ctx, "helloServlet", new HelloServlet());
        ctx.addServletMappingDecoded("/hello", "helloServlet");

        Tomcat.addServlet(ctx, "forwardServlet", new ForwardServlet());
        ctx.addServletMappingDecoded("/forward", "forwardServlet");

        Tomcat.addServlet(ctx, "homeServlet", new HomeServlet());
        ctx.addServletMappingDecoded("", "homeServlet");

        Tomcat.addServlet(ctx, "downloadServlet", new DownloadServlet());
        ctx.addServletMappingDecoded("/download", "downloadServlet");

        Tomcat.addServlet(ctx, "userServlet", new UserServlet());
        ctx.addServletMappingDecoded("/user", "userServlet");

        Tomcat.addServlet(ctx, "user2Servlet", new User2Servlet());
        ctx.addServletMappingDecoded("/user2", "user2Servlet");
    }
}