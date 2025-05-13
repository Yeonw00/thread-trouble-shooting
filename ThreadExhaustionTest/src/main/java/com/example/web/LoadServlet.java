package com.example.web;

import java.io.IOException;
import java.sql.DriverManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/load")
public class LoadServlet extends HttpServlet {
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Thread.sleep(5000);  // 스레드 점유 유도
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        resp.getWriter().write("OK\n");
    }
}
