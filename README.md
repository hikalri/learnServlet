# 使用嵌入式Tomcat的Web应用程序

这是一个使用嵌入式Tomcat服务器的Java Web应用程序，已经从传统的WAR包部署方式转换为JAR包方式。

## 项目结构

- `src/main/java/com/example/Application.java` - 主应用程序类，用于启动嵌入式Tomcat
- `src/main/java/com/example/TomcatConfig.java` - Tomcat配置类，用于程序化注册Servlet
- `src/main/java/com/example/HelloServlet.java` - Hello World Servlet
- `src/main/java/com/example/DownloadServlet.java` - 文件下载Servlet
- `src/main/java/com/example/PropertiesLoader.java` - 属性文件加载工具类
- `src/main/webapp/` - Web资源目录
- `src/main/resources/config.properties` - 应用程序配置文件

## 如何运行

### 方法1：使用启动脚本（Windows）

1. 双击运行 `start.bat` 文件（使用mvnd和PowerShell）
2. 等待应用程序启动
3. 在浏览器中访问 `http://localhost:8080`

### 方法2：手动命令行（使用mvnd）

1. 编译项目：
   ```
   mvnd clean compile
   ```

2. 打包项目：
   ```
   mvnd package
   ```

3. 运行应用程序（设置UTF-8编码）：
   ```
   java -Dfile.encoding=UTF-8 -Dconsole.encoding=UTF-8 -cp target/mywebapp.jar com.example.Application
   ```

### 方法3：使用PowerShell

1. 在PowerShell中编译项目：
   ```
   mvnd clean compile
   ```

2. 在PowerShell中打包项目：
   ```
   mvnd package
   ```

3. 在PowerShell中运行应用程序：
   ```
   java -Dfile.encoding=UTF-8 -Dconsole.encoding=UTF-8 -cp target/mywebapp.jar com.example.Application
   ```

## 访问端点

- 主页：`http://localhost:8080`
- Hello Servlet：`http://localhost:8080/hello` 或 `http://localhost:8080/api/hello`
- 下载配置文件：`http://localhost:8080/download`

## 主要变更

1. **打包方式**：从WAR改为JAR
2. **依赖管理**：添加了嵌入式Tomcat相关依赖
3. **Servlet注册**：从注解方式改为程序化注册
4. **启动方式**：从外部容器启动改为内嵌容器启动

## 技术栈

- Java 8
- Servlet 4.0.1
- 嵌入式Tomcat 9.0.58
- Jackson 2.13.0（JSON处理）
- Maven 3.x

## 注意事项

- 确保已安装Java 8或更高版本
- 确保已安装Maven 3.x或Maven Daemon (mvnd)
- 启动脚本使用mvnd和PowerShell，确保系统已安装这些工具
- 默认端口为8080，如需修改请编辑Application.java中的端口号
- 如果遇到控制台乱码问题，请确保终端支持UTF-8编码
- 启动脚本已设置UTF-8编码，建议使用start.bat启动应用程序
- mvnd是Maven的守护进程版本，构建速度比传统mvn更快