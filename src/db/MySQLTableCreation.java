package db;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.*;
import db.MySQLDBUtil;
import external.AlphaVantageAPI;

import java.sql.Connection;

public class MySQLTableCreation {
	public static List<String> getPermutation(String[] alphabet, int length){
		List<String> res = new ArrayList<>();
		for (int i=1; i <=length; i++) {
			List<String> array = new ArrayList<>();
			backtrack(alphabet, "", i, array);
			res.addAll(array);
		}
		
		return res;
	}
	
	public static void backtrack(String[] alphabet, String curr, int length, List<String> output){
		if (curr.length() == length) {
			output.add(curr);
			return;
		}
		
		for(int i=0; i < alphabet.length; i++) {
			curr += alphabet[i];
			backtrack(alphabet, curr, length, output);
			curr = curr.substring(0, curr.length() - 1);
		}
	}
	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
			Connection conn = DriverManager.getConnection(MySQLDBUtil.URL);
			AlphaVantageAPI api = new AlphaVantageAPI();
			if (conn == null) {
				System.out.println("Connection on SQL is null");
				return;
			}
			Statement statement = conn.createStatement();
			String sql = "DROP TABLE IF EXISTS log";
			statement.executeUpdate(sql);
			
			sql = "DROP TABLE IF EXISTS watchlists";
			statement.executeUpdate(sql);
			
			sql = "DROP TABLE IF EXISTS stocks";
			statement.executeUpdate(sql);
 
			sql = "DROP TABLE IF EXISTS users";
			statement.executeUpdate(sql);
			
//			Runtime.getRuntime().exec("/usr/local/bin/python3 ./StockDataCreation/stockDataCreation.py");
			
			sql = "CREATE TABLE users ("
					+ "user_id VARCHAR(255) NOT NULL,"
					+ "password VARCHAR(255) NOT NULL,"
					+ "first_name VARCHAR(255) NOT NULL,"
					+ "last_name VARCHAR (255) NOT NULL,"
					+ "balance DOUBLE DEFAULT 0.0,"
					+ "asset_value DOUBLE DEFAULT 0.0,"
					+ "total_value DOUBLE DEFAULT 0.0,"
					+ "PRIMARY KEY (user_id)"
					+ ")";
			
			statement.executeUpdate(sql);
			
			sql = "CREATE TABLE stocks ("
					+ "user_id VARCHAR(255) NOT NULL,"
					+ "symbol VARCHAR (255) NOT NULL,"
					+ "position DOUBLE,"
					+ "last_updated_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
					+ "PRIMARY KEY (user_id, symbol)"
					+ ")";
			
			//Action could only be buy or sell
			statement.executeUpdate(sql);
			sql = "CREATE TABLE log ("
					+ "action_id VARCHAR(255) NOT NULL,"
					+ "user_id VARCHAR(255) NOT NULL,"
					+ "symbol VARCHAR (255) NOT NULL,"
					+ "action VARCHAR (255) NOT NULL,"
					+ "action_vol INT NOT NULL,"
					+ "time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
					+ "price DOUBLE,"
					+ "PRIMARY KEY (action_id)"
					+ ")";
			statement.executeUpdate(sql);
			
			sql = "CREATE TABLE watchlists ("
					+ "user_id VARCHAR(255) NOT NULL,"
					+ "symbol VARCHAR (255) NOT NULL,"
					+ "PRIMARY KEY (user_id,symbol)"
					+ ")";
			statement.executeUpdate(sql);
			
			boolean createTickers  = false;
			if (createTickers) {
				sql = "DROP TABLE IF EXISTS tickers";
				statement.executeUpdate(sql);
				sql = "CREATE TABLE tickers ("
						+ "symbol VARCHAR(255) NOT NULL,"
						+ "name VARCHAR(255) NOT NULL,"
						+ "PRIMARY KEY (symbol)"
						+ ")";
				statement.executeUpdate(sql);
				String[] alphabet = new String[] {
				"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"
				};
				List<String> fragments = getPermutation(alphabet, 2);
				HashMap<String, String> tickerInfo = api.getAllSymbols(fragments);
				for (Map.Entry<String, String> entry : tickerInfo.entrySet()) {
					String symbol = entry.getKey();
					String name = entry.getValue();
					sql = "INSERT INTO tickers(symbol, name) VALUES(?,?)";
					PreparedStatement pStatement = conn.prepareStatement(sql);
					pStatement.setString(1, symbol);
					pStatement.setString(2, name);
					pStatement.execute();
				}
			}
			System.out.println("DataBase Creation Complete");
			
		}
		
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
}
