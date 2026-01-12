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
        // BaseDir 决定了 Tomcat 运行时的临时文件存放位置（例如 Work 目录、临时解压目录等）
        tomcat.setBaseDir(new File(".").getAbsolutePath());
        tomcat.setPort(8080);

        // 如果你只是运行 Servlet，使用 addContext 就足够了，它非常轻量
        // addContext(..., "src/main/webapp/")：告诉 Tomcat 静态资源（HTML/CSS/JS）在哪里
        // 例外：如果你在 webapp 下创建了一个名为 WEB-INF 的文件夹，Tomcat 会严格禁止外部通过 URL 直接访问这个文件夹里的任何内容
        StandardContext ctx = (StandardContext) tomcat.addContext("",
            new File("src/main/webapp/").getAbsolutePath());

        // 设置URI编码为UTF-8
        tomcat.getConnector().setURIEncoding(StandardCharsets.UTF_8.name());
        tomcat.getConnector().setUseBodyEncodingForURI(true);

        // 声明编译后的类路径位置
        // 根据 Servlet 规范，它会去虚拟路径 /WEB-INF/classes 下寻找对应的 .class 文件
        // DirResourceSet(..., "/WEB-INF/classes", "target/classes", "/")：告诉 Tomcat
        // 编译后的业务代码在哪里
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