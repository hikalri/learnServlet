package com.example;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class HelloServlet extends HttpServlet {

    private ObjectMapper objectMapper;
    private PropertiesLoader propertiesLoader;

    @Override
    public void init() throws ServletException {
        super.init();
        // 配置ObjectMapper以支持UTF-8编码
        objectMapper = new ObjectMapper();
        // 确保Jackson使用UTF-8编码
        objectMapper.getFactory().setCharacterEscapes(null); // 使用默认字符转义

        // 加载properties文件
        propertiesLoader = new PropertiesLoader("config.properties");
        // 打印所有加载的属性（可选，用于调试）
        propertiesLoader.printAllProperties();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 设置响应类型为 JSON
        response.setContentType("application/json;charset=UTF-8");
        // 添加CORS支持
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");

        try (PrintWriter out = response.getWriter()) {
            // 创建 JSON 数据
            Map<String, Object> jsonData = new HashMap<>();

            // 获取所有请求参数
            Map<String, String[]> parameterMap = request.getParameterMap();

            if (parameterMap != null && !parameterMap.isEmpty()) {
                // 遍历所有参数，支持任意字段
                for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                    String paramName = entry.getKey();
                    String[] paramValues = entry.getValue();

                    // 如果参数有多个值，只取第一个
                    if (paramValues != null && paramValues.length > 0) {
                        jsonData.put(paramName, paramValues[0]);
                    }
                }
            } else {
                // 默认 JSON 数据
                jsonData.put("message", "Hello, Servlet!");
                jsonData.put("description", "这是一个简单的 JSON 响应示例");
                jsonData.put("timestamp", new java.util.Date().toString());
            }

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

            // 使用Jackson库转换为JSON字符串，确保UTF-8编码
            String jsonResponse = new String(objectMapper.writeValueAsString(jsonData).getBytes("UTF-8"), "UTF-8");
            out.print(jsonResponse);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 设置响应类型为 JSON
        response.setContentType("application/json;charset=UTF-8");

        // 添加CORS支持
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");

        try (PrintWriter out = response.getWriter()) {
            // 创建 JSON 数据
            Map<String, Object> jsonData = new HashMap<>();

            // 获取请求内容类型
            String contentType = request.getContentType();

            if (contentType != null && contentType.contains("application/json")) {
                // 处理JSON请求体
                try {
                    // 读取请求体，确保使用UTF-8编码
                    StringBuilder sb = new StringBuilder();
                    String line;
                    try (BufferedReader reader = request.getReader()) {
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }
                    }

                    // 解析JSON
                    String requestBody = sb.toString();
                    if (!requestBody.isEmpty()) {
                        // 使用Jackson解析JSON，确保UTF-8编码
                        Map<String, Object> jsonRequest = objectMapper.readValue(requestBody, new TypeReference<Map<String, Object>>() {});
                        jsonData.putAll(jsonRequest);
                    }
                } catch (Exception e) {
                    // JSON解析失败，返回错误信息
                    jsonData.put("error", "Invalid JSON format");
                    jsonData.put("message", e.getMessage());
                }
            } else {
                // 处理表单数据
                Map<String, String[]> parameterMap = request.getParameterMap();

                if (parameterMap != null && !parameterMap.isEmpty()) {
                    // 遍历所有参数，支持任意字段
                    for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                        String paramName = entry.getKey();
                        String[] paramValues = entry.getValue();

                        // 如果参数有多个值，只取第一个
                        if (paramValues != null && paramValues.length > 0) {
                            jsonData.put(paramName, paramValues[0]);
                        }
                    }
                } else {
                    // 默认 JSON 数据
                    jsonData.put("message", "Hello, Servlet!");
                    jsonData.put("description", "这是一个简单的 JSON 响应示例");
                    jsonData.put("timestamp", new java.util.Date().toString());
                }
            }

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

            // 使用Jackson库转换为JSON字符串，确保UTF-8编码
            String jsonResponse = new String(objectMapper.writeValueAsString(jsonData).getBytes("UTF-8"), "UTF-8");
            out.print(jsonResponse);
        }
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 处理预检请求
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}