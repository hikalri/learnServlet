package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        String idStr = req.getParameter("id");
        if (idStr != null) {
            try {
                Long id = Long.parseLong(idStr);
                User user = userDAO.get(id);
                if (user != null) {
                    resp.getWriter().write(objectMapper.writeValueAsString(user));
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write("{\"error\":\"User not found\"}");
                }
            } catch (NumberFormatException e) {
                 resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                 resp.getWriter().write("{\"error\":\"Invalid ID\"}");
            }
        } else {
            List<User> users = userDAO.getAll();
            resp.getWriter().write(objectMapper.writeValueAsString(users));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");

        try {
            User user = objectMapper.readValue(req.getReader(), User.class);
            userDAO.add(user);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("{\"message\":\"User created\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");

        try {
            User user = objectMapper.readValue(req.getReader(), User.class);
            userDAO.update(user);
            resp.getWriter().write("{\"message\":\"User updated\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
             resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        String idStr = req.getParameter("id");
        if (idStr != null) {
             try {
                Long id = Long.parseLong(idStr);
                userDAO.delete(id);
                resp.getWriter().write("{\"message\":\"User deleted\"}");
            } catch (NumberFormatException e) {
                 resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                 resp.getWriter().write("{\"error\":\"Invalid ID\"}");
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"ID required\"}");
        }
    }
}