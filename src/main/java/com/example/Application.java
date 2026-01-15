package com.example;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.ContextConfig;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.JarResourceSet;
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
        // addContext告诉 Tomcat 静态资源（HTML/CSS/JS）在哪里
        // 例外：如果你在 context目录 下创建了一个名为 WEB-INF 的文件夹，Tomcat 会严格禁止外部通过 URL 直接访问这个文件夹里的任何内容
        StandardContext ctx = (StandardContext) tomcat.addContext("",
                new File(System.getProperty("java.io.tmpdir")).getAbsolutePath());
        // 设置URI编码为UTF-8
        tomcat.getConnector().setURIEncoding(StandardCharsets.UTF_8.name());
        tomcat.getConnector().setUseBodyEncodingForURI(true);

        // 这一步告诉 Tomcat 去执行生命周期中的配置解析，包括扫描注解
        // 识别 META-INF/resources 路径 用于生产环境，自动处理 CLASSPATH 下的资源
        ctx.addLifecycleListener(new ContextConfig());

        WebResourceRoot resources = new StandardRoot(ctx);

        // 获取当前运行的 JAR 包或类路径路径
        File jarFile = new File(Application.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        String codePath = jarFile.getAbsolutePath();
        if (codePath.endsWith(".jar")) {
            // 生产环境：如果是以 JAR 运行，添加 JarResourceSet
            // 用于识别 Servlet 注解
            resources.addJarResources(new JarResourceSet(resources, "/WEB-INF/classes", codePath, "/"));
        } else {
            // 开发环境
            // 1. 映射静态资源
            File webappDir = new File("src/main/webapp");
            if (webappDir.exists()) {
                resources.addPreResources(new DirResourceSet(resources, "/", webappDir.getAbsolutePath(), "/"));
            }
            // 根据 Servlet 规范，它会去虚拟路径 /WEB-INF/classes 下寻找对应的 .class 文件
            // 2. 映射编译后的类（核心：用于热更新和注解扫描）
            File classesDir = new File("target/classes");
            if (classesDir.exists()) {
                resources.addPreResources(
                        new DirResourceSet(resources, "/WEB-INF/classes", classesDir.getAbsolutePath(), "/"));
            }
        }

        ctx.setResources(resources);
        // 这里通过编程式方式设置了 servlet，所以不设置 /WEB-INF/classes 虚拟路径其实也没关系
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