package db;

import java.sql.DriverManager;
import java.sql.Statement;

import com.mysql.cj.xdevapi.Table;

import db.MySQLDBUtil;

import java.sql.Connection;

public class MySQLTableCreation {
	public static void main(String[] args) {
		try {
			System.out.println("Connecting to " + MySQLDBUtil.URL);
			Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
			Connection conn = DriverManager.getConnection(MySQLDBUtil.URL);
			if (conn == null) {
				System.out.println("Connection on SQL is null");
				return;
			}
			
			Statement statement = conn.createStatement();
			String sql = "DROP TABLE IF EXISTS log";
			statement.executeUpdate(sql);
			

			sql = "DROP TABLE IF EXISTS stocks";
			statement.executeUpdate(sql);
			
			sql = "DROP TABLE IF EXISTS users";
			statement.executeUpdate(sql);
			
			sql = "CREATE TABLE users ("
					+ "user_id VARCHAR(255) NOT NULL,"
					+ "password VARCHAR(255) NOT NULL,"
					+ "first_name VARCHAR(255) NOT NULL,"
					+ "last_name VARCHAR (255) NOT NULL,"
					+ "balance DOUBLE,"
					+ "PRIMARY KEY (user_id)"
					+ ")";
			
			statement.executeUpdate(sql);
			
			sql = "CREATE TABLE stocks ("
					+ "user_id VARCHAR(255) NOT NULL,"
					+ "symbol VARCHAR (255) NOT NULL,"
					+ "position DOUBLE,"
					+ "last_updated_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
					+ "PRIMARY KEY (user_id, symbol),"
					+ "FOREIGN KEY (user_id) REFERENCES users(user_id)"
					+ ")";
			
			//Action could only be buy or sell
			statement.executeUpdate(sql);
			sql = "CREATE TABLE log ("
					+ "user_id VARCHAR(255) NOT NULL,"
					+ "symbol VARCHAR (255) NOT NULL,"
					+ "action VARCHAR (255) NOT NULL,"
					+ "action_vol INT NOT NULL,"
					+ "time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
					+ "price DOUBLE,"
					+ "PRIMARY KEY (user_id, symbol),"
					+ "FOREIGN KEY (user_id) REFERENCES users(user_id)"
					+ ")";
			
			statement.executeUpdate(sql);
			
			System.out.println("MySQL Table Creation Complete");
			
			boolean mock = true;
			if (mock) {
				
			}
		}
		
		catch (Exception e){
			e.printStackTrace();
		}
	}
}
