package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * 用于加载properties文件的工具类
 */
public class PropertiesLoader {
    private Properties properties;

    /**
     * 构造函数，加载指定的properties文件
     * @param fileName properties文件名，相对于classpath
     */
    public PropertiesLoader(String fileName) {
        this.properties = new Properties();
        loadProperties(fileName);
    }

    /**
     * 加载properties文件
     * @param fileName properties文件名
     */
    private void loadProperties(String fileName) {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                System.out.println("无法找到properties文件: " + fileName);
                return;
            }
            // 使用UTF-8编码加载properties文件
            properties.load(new InputStreamReader(input, StandardCharsets.UTF_8));
            System.out.println("成功加载properties文件: " + fileName);
        } catch (IOException ex) {
            System.out.println("加载properties文件时出错: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * 获取属性值
     * @param key 属性键
     * @return 属性值，如果不存在则返回null
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * 获取属性值，如果不存在则返回默认值
     * @param key 属性键
     * @param defaultValue 默认值
     * @return 属性值
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * 获取所有属性
     * @return Properties对象
     */
    public Properties getAllProperties() {
        return properties;
    }

    /**
     * 打印所有属性
     */
    public void printAllProperties() {
        System.out.println("===== Properties内容 =====");
        properties.forEach((key, value) -> System.out.println(key + ": " + value));
        System.out.println("=========================");
    }
}