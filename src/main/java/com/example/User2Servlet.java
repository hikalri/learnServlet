package com.example;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class User2Servlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");

        String type = req.getParameter("type"); // "success" or "fail"

        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false); // 开启事务

            // 1. 添加第一个用户
            User user1 = new User(null, "TxUser1", "tx1@example.com", 20);
            userDAO.add(conn, user1);

            // 2. 模拟失败
            if ("fail".equals(type)) {
                throw new RuntimeException("模拟的异常，触发回滚");
            }

            // 3. 添加第二个用户
            User user2 = new User(null, "TxUser2", "tx2@example.com", 22);
            userDAO.add(conn, user2);

            conn.commit(); // 提交事务
            resp.getWriter().write("{\"message\":\"事务提交成功，已添加两个用户\", \"status\":\"success\"}");

        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); // 回滚事务
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"message\":\"事务回滚成功: " + e.getMessage() + "\", \"status\":\"error\"}");
        } finally {
            if (conn != null) {
                DBUtil.releaseConnection(conn);
            }
        }
    }
}