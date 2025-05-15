package com.example.web;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/load")
public class LoadServlet extends HttpServlet {
	
	private Properties dbProps = new Properties();
	private static final Logger logger = Logger.getLogger(LoadServlet.class.getName());

    @Override
    public void init() throws ServletException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (in == null) throw new RuntimeException("application.properties not found");
            dbProps.load(in);
        } catch (IOException e) {
            throw new ServletException("Failed to load DB properties", e);
        }
    }
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String url = dbProps.getProperty("db.url");
		String user = dbProps.getProperty("db.user");
		String pass = dbProps.getProperty("db.password");
		
		// 장애 상태 진입 로그 출력
		int threadCount = Thread.activeCount();
        System.err.println("현재 스레드 수: " + threadCount);
        if (threadCount > 300) {
            logger.severe("과부하 경고: Thread count = " + threadCount);
            throw new RuntimeException("Tomcat 스레드 고갈 감지: " + threadCount);
        }
        
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection conn = DriverManager.getConnection(url, user, pass);
            PreparedStatement stmt = conn.prepareStatement("SELECT 1 FROM DUAL");
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Thread.sleep(10000); //  10초간 스레드+DB 커넥션 점유
            }

            resp.getWriter().write("OK\n");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("ERROR: " + e.getMessage());
        }
    }
}
