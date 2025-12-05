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
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(new File(".").getAbsolutePath());
        tomcat.setPort(8080);

        StandardContext ctx = (StandardContext) tomcat.addContext("",
            new File("src/main/webapp/").getAbsolutePath());

        // 设置URI编码为UTF-8
        tomcat.getConnector().setURIEncoding(StandardCharsets.UTF_8.name());
        tomcat.getConnector().setUseBodyEncodingForURI(true);

        // 声明编译后的类路径位置
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
            new File("target/classes").getAbsolutePath(), "/"));
        ctx.setResources(resources);

        TomcatConfig.configureServlets(ctx);

        // 添加关闭钩子，确保连接池正确关闭
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("正在关闭应用...");
            ConnectionPool.getInstance().closeAll();
        }));

        tomcat.start();
        System.out.println("服务器已启动，访问地址: http://localhost:8080");
        System.out.println(ConnectionPool.getInstance().getPoolStatus());
        tomcat.getServer().await();
    }
}