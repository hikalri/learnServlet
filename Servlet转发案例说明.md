# Servlet转发案例

本案例演示了如何在Java Servlet中使用请求转发(Request Forward)功能，将请求从一个Servlet转发到另一个Servlet。

## 项目结构

```
src/main/java/com/example/
├── HelloServlet.java      # 目标Servlet，处理实际业务逻辑
├── ForwardServlet.java    # 转发Servlet，将请求转发到HelloServlet
└── ...

src/main/webapp/
├── WEB-INF/
│   └── web.xml            # Servlet配置文件
├── test-forward.html      # 测试页面
└── ...
```

## 功能说明

### 1. HelloServlet
- 处理GET和POST请求
- 返回JSON格式的响应
- 支持请求参数和JSON请求体
- 能够识别并显示转发过来的属性信息

### 2. ForwardServlet
- 接收客户端请求
- 在转发前设置请求属性
- 将请求转发到HelloServlet处理

### 3. 转发流程
1. 客户端发送请求到`/forward`
2. ForwardServlet接收请求，设置转发属性
3. ForwardServlet将请求转发到`/hello`
4. HelloServlet处理请求并返回响应
5. 客户端收到最终响应

## 配置说明

在`web.xml`中配置了两个Servlet：

```xml
<!-- HelloServlet 配置 -->
<servlet>
    <servlet-name>HelloServlet</servlet-name>
    <servlet-class>com.example.HelloServlet</servlet-class>
</servlet>

<servlet-mapping>
    <servlet-name>HelloServlet</servlet-name>
    <url-pattern>/hello</url-pattern>
</servlet-mapping>

<!-- ForwardServlet 配置 -->
<servlet>
    <servlet-name>ForwardServlet</servlet-name>
    <servlet-class>com.example.ForwardServlet</servlet-class>
</servlet>

<servlet-mapping>
    <servlet-name>ForwardServlet</servlet-name>
    <url-pattern>/forward</url-pattern>
</servlet-mapping>
```

## 测试方法

### 1. 使用测试页面
访问 `http://localhost:8080/test-forward.html`，页面提供了三种测试方式：
- 直接访问HelloServlet
- 通过ForwardServlet转发访问
- 带参数的转发测试

### 2. 使用curl命令

直接访问HelloServlet：
```bash
curl http://localhost:8080/hello
```

通过ForwardServlet转发：
```bash
curl http://localhost:8080/forward
```

带参数的转发：
```bash
curl "http://localhost:8080/forward?name=test&value=123"
```

## 响应示例

### 直接访问HelloServlet的响应：
```json
{
  "message": "Hello, Servlet!",
  "description": "这是一个简单的 JSON 响应示例",
  "timestamp": "Mon Dec 02 14:11:28 CST 2025",
  "isForwarded": false
}
```

### 通过ForwardServlet转发的响应：
```json
{
  "message": "Hello, Servlet!",
  "description": "这是一个简单的 JSON 响应示例",
  "timestamp": "Mon Dec 02 14:11:28 CST 2025",
  "forwardMessage": "这是从ForwardServlet转发的请求",
  "forwardTime": "Mon Dec 02 14:11:28 CST 2025",
  "isForwarded": true
}
```

## 转发与重定向的区别

| 特性 | 转发(Forward) | 重定向(Redirect) |
|------|---------------|------------------|
| URL变化 | 客户端URL不变 | 客户端URL改变 |
| 请求次数 | 一次请求 | 两次请求 |
| 请求属性 | 可以传递 | 不可以传递 |
| 服务器内部 | 是 | 否 |
| 性能 | 更高 | 较低 |

## 关键代码说明

### ForwardServlet中的转发代码：
```java
// 设置请求属性
request.setAttribute("forwardMessage", "这是从ForwardServlet转发的请求");
request.setAttribute("forwardTime", new java.util.Date().toString());

// 执行转发
request.getRequestDispatcher("/hello").forward(request, response);
```

### HelloServlet中检查转发属性的代码：
```java
// 检查是否有转发的属性
String forwardMessage = (String) request.getAttribute("forwardMessage");
String forwardTime = (String) request.getAttribute("forwardTime");

if (forwardMessage != null) {
    jsonData.put("forwardMessage", forwardMessage);
    jsonData.put("forwardTime", forwardTime);
    jsonData.put("isForwarded", true);
} else {
    jsonData.put("isForwarded", false);
}
```

## 运行项目

1. 确保已安装Java和Maven
2. 在项目根目录运行：`mvn clean install`
3. 运行项目：`mvn tomcat7:run` 或使用IDE运行
4. 访问 `http://localhost:8080/test-forward.html` 进行测试