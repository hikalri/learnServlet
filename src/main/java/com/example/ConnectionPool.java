package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 简单版数据库连接池
 */
public class ConnectionPool {

    private static ConnectionPool instance;
    private BlockingQueue<Connection> connectionPool;
    private String url;
    private String username;
    private String password;
    private int maxPoolSize = 10;
    private int initialSize = 3;

    private ConnectionPool() {
        // 从配置文件加载数据库配置
        PropertiesLoader loader = new PropertiesLoader("config.properties");
        this.url = loader.getProperty("db.url");
        this.username = loader.getProperty("db.username");
        this.password = loader.getProperty("db.password");

        // 初始化连接池
        connectionPool = new LinkedBlockingQueue<>(maxPoolSize);
        initializePool();
    }

    /**
     * 获取连接池单例实例
     */
    public static synchronized ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    /**
     * 初始化连接池，创建初始连接
     */
    private void initializePool() {
        System.out.println("初始化数据库连接池，初始连接数: " + initialSize);
        for (int i = 0; i < initialSize; i++) {
            try {
                Connection conn = createNewConnection();
                if (conn != null) {
                    connectionPool.offer(conn);
                    System.out.println("创建初始连接 " + (i + 1) + "/" + initialSize);
                }
            } catch (SQLException e) {
                System.err.println("创建初始连接失败: " + e.getMessage());
            }
        }
        System.out.println("连接池初始化完成，当前可用连接数: " + connectionPool.size());
    }

    /**
     * 创建新的数据库连接
     */
    private Connection createNewConnection() throws SQLException {
        long start = System.currentTimeMillis();
        try {
            // 加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, username, password);
            long cost = System.currentTimeMillis() - start;
            System.out.println("创建新数据库连接耗时: " + cost + " ms");
            return conn;
        } catch (ClassNotFoundException e) {
            throw new SQLException("数据库驱动加载失败", e);
        }
    }

    /**
     * 从连接池获取连接
     */
    public Connection getConnection() throws SQLException {
        long start = System.currentTimeMillis();
        try {
            // 尝试从池中获取连接，最多等待5秒
            Connection conn = connectionPool.poll(5, TimeUnit.SECONDS);
            if (conn != null) {
                // 检查连接是否有效
                if (conn.isValid(3)) {
                    long cost = System.currentTimeMillis() - start;
                    System.out.println("从连接池获取连接耗时: " + cost + " ms，剩余连接: " + connectionPool.size());
                    return conn;
                } else {
                    // 连接无效，创建新连接
                    System.out.println("连接已失效，创建新连接");
                    return createNewConnection();
                }
            } else {
                // 池中没有可用连接，且未达到最大连接数，创建新连接
                if (connectionPool.size() < maxPoolSize) {
                    System.out.println("连接池已满，创建新连接");
                    return createNewConnection();
                } else {
                    throw new SQLException("连接池已满且无法创建新连接，请稍后再试");
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SQLException("获取连接被中断", e);
        }
    }

    /**
     * 将连接归还到连接池
     */
    public void releaseConnection(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    // 重置连接状态
                    if (!conn.getAutoCommit()) {
                        conn.rollback();
                        conn.setAutoCommit(true);
                    }

                    // 尝试归还到池中
                    if (connectionPool.offer(conn)) {
                        System.out.println("连接已归还到连接池，当前可用连接: " + connectionPool.size());
                    } else {
                        // 池已满，直接关闭连接
                        conn.close();
                        System.out.println("连接池已满，直接关闭连接");
                    }
                } else {
                    System.out.println("连接已关闭，无法归还到连接池");
                }
            } catch (SQLException e) {
                System.err.println("归还连接时出错: " + e.getMessage());
                try {
                    conn.close();
                } catch (SQLException ex) {
                    System.err.println("关闭连接失败: " + ex.getMessage());
                }
            }
        }
    }

    /**
     * 关闭连接池中的所有连接
     */
    public void closeAll() {
        System.out.println("开始关闭连接池...");
        Connection conn;
        while ((conn = connectionPool.poll()) != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                    System.out.println("关闭一个连接");
                }
            } catch (SQLException e) {
                System.err.println("关闭连接时出错: " + e.getMessage());
            }
        }
        System.out.println("连接池已关闭");
    }

    /**
     * 获取连接池状态信息
     */
    public String getPoolStatus() {
        return String.format("连接池状态 - 可用连接: %d/%d", connectionPool.size(), maxPoolSize);
    }
}