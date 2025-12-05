package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 数据库工具类
 */
public class DBUtil {
    private static Properties properties;

    static {
        try {
            // 加载配置文件
            PropertiesLoader loader = new PropertiesLoader("config.properties");
            properties = loader.getAllProperties();
            // 加载驱动
            Class.forName(properties.getProperty("db.driver"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("加载数据库驱动失败", e);
        }
    }

    /**
     * 获取数据库连接
     * @return 数据库连接对象
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return ConnectionPool.getInstance().getConnection();
    }

    /**
     * 关闭资源
     * @param conn 连接对象
     * @param pstmt 预编译语句对象
     * @param rs 结果集对象
     */
    public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            // 归还连接到连接池，而不是直接关闭
            ConnectionPool.getInstance().releaseConnection(conn);
        }
    }

    /**
     * 关闭资源 (无结果集)
     * @param conn 连接对象
     * @param pstmt 预编译语句对象
     */
    public static void close(Connection conn, PreparedStatement pstmt) {
        close(conn, pstmt, null);
    }

    /**
     * 仅归还连接到连接池
     * @param conn 连接对象
     */
    public static void releaseConnection(Connection conn) {
        if (conn != null) {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
    }
}