package com.example;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 用于下载properties文件的Servlet
 */
public class DownloadServlet extends HttpServlet {

    private static final String PROPERTIES_FILE = "config.properties";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 设置请求编码为UTF-8
        request.setCharacterEncoding("UTF-8");

        // 添加CORS支持
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");

        // 获取要下载的文件名，如果没有指定则使用默认的properties文件
        String fileName = request.getParameter("file");
        if (fileName == null || fileName.trim().isEmpty()) {
            fileName = PROPERTIES_FILE;
        }

        // 确保文件名以.properties结尾
        if (!fileName.endsWith(".properties")) {
            fileName += ".properties";
        }

        // 从classpath获取文件输入流
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {

            if (inputStream == null) {
                // 文件不存在，返回404错误
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"error\": \"文件不存在: " + fileName + "\"}");
                return;
            }

            // 设置响应头
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("UTF-8");

            // 设置Content-Disposition头，指示浏览器下载文件
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");

            // 设置内容长度（如果可能）
            // 注意：对于classpath资源，我们可能无法预先知道大小，所以跳过这个设置

            // 将文件内容写入响应输出流
            try (OutputStream outputStream = response.getOutputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
            }

            System.out.println("文件下载成功: " + fileName);

        } catch (IOException e) {
            // 处理IO异常
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\": \"下载文件时出错: " + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // POST请求重定向到GET处理
        doGet(request, response);
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