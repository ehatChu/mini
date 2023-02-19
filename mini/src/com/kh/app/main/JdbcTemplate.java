package com.kh.app.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcTemplate {
	//커넥션 객체 리턴
	public static Connection getConnection() throws SQLException {
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String id ="C##KH";
		String pwd = "KH";
		Connection conn = DriverManager.getConnection(url,id,pwd);
		return conn;
	}
}