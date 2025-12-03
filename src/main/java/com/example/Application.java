package com.example;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * 应用程序主类，用于启动嵌入式Tomcat服务器
 */
public class Application {

    public static void main(String[] args) throws Exception {
        // 创建Tomcat实例
        Tomcat tomcat = new Tomcat();

        // 设置基础目录
        File baseDir = new File(".");
        tomcat.setBaseDir(baseDir.getAbsolutePath());

        // 设置端口
        tomcat.setPort(8080);

        // 设置连接器
        tomcat.getConnector();

        // 设置基础目录
        String webappDirLocation = "src/main/webapp/";
        StandardContext ctx = (StandardContext) tomcat.addContext("",
            new File(webappDirLocation).getAbsolutePath());

        // 设置URI编码为UTF-8
        tomcat.getConnector().setURIEncoding(StandardCharsets.UTF_8.name());
        tomcat.getConnector().setUseBodyEncodingForURI(true);

        // 声明编译后的类路径位置
        File additionWebInfClasses = new File("target/classes");
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
            additionWebInfClasses.getAbsolutePath(), "/"));
        ctx.setResources(resources);

        // 配置Servlet
        TomcatConfig.configureServlets(ctx);

        // 启动Tomcat
        tomcat.start();
        System.out.println("服务器已启动，访问地址: http://localhost:8080");

        // 保持服务器运行
        tomcat.getServer().await();
    }
}