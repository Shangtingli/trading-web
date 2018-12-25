package db;
	
import java.sql.*;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

import db.MySQLDBUtil;
import db.DataMock;
import entity.Item;
import external.AlphaVantageAPI;

public class MySQLConnection {
	private int action_id;
	private Connection conn;
	
	//Constructor for MySQLConnection
	public MySQLConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
			conn = DriverManager.getConnection(MySQLDBUtil.URL);
			String sql = "SELECT COUNT(*) AS count FROM log;";
			PreparedStatement statement =conn.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			rs.next();
			action_id = rs.getInt("count");
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	
	//Close the MySQLConnection
	public void close() {
		if (conn == null) {
			System.out.println("DBConnection is Already NULL");
		}
		
		try {
			conn.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * See if a user already exists
	 */
	public boolean checkUser(String userid) {
		if (conn == null) {
			System.out.println("DBConnection is NULL");
			return false;
		}
		
		try {
			String sql = "SELECT COUNT(*) AS count FROM users WHERE user_id = ?";
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setString(1, userid);
			ResultSet res = pStatement.executeQuery();
			res.next();
			if (res.getInt("count") > 0) {
				return true;
			}
			else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	/*
	 * Find Suggestions for Stock Symbols
	 */
	public JSONObject findSuggestions(String fragment) {
		if (conn == null) {
			System.out.println("DBConnection is NULL");
			return new JSONObject();
		}
		
		try {
			JSONObject res = new JSONObject();
			String sql = "SELECT * FROM tickers WHERE symbol LIKE ? LIMIT 7";
			PreparedStatement pStatement = conn.prepareStatement(sql);
			String part = "%" + fragment + "%";
			pStatement.setString(1, part);
			System.out.println(pStatement.toString());
			System.out.println(fragment);
			ResultSet rs = pStatement.executeQuery();
			while(rs.next()) {
				res.put(rs.getString("symbol"),rs.getString("name"));
			}
			
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new JSONObject();
	}
	/*
	 * Find Actual Results from Stock Symbols
	 */
	public JSONObject findStock(String symbol) {
		if (conn == null) {
			System.out.println("DBConnection is NULL");
			return new JSONObject();
		}
		
		try {
			JSONObject res = new JSONObject();
			String sql = "SELECT * FROM tickers WHERE symbol = ?";
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setString(1, symbol);
			ResultSet rs = pStatement.executeQuery();
			while(rs.next()) {
				res.put(rs.getString("symbol"),rs.getString("name"));
			}
			
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new JSONObject();
	}
	/*
	 * Register a new user
	 */
	public void addUser(String userid, String password, String firstname, String lastname) {
		if (conn == null) {
			System.out.println("DBConnection is NULL");
			return;
		}
		
		try {
			String sql = "INSERT INTO users(user_id,password,first_name,last_name,balance) VALUES(?,?,?,?,0)";
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setString(1, userid);
			pStatement.setString(2, password);
			pStatement.setString(3, firstname);
			pStatement.setString(4, lastname);
			System.out.println(pStatement.toString());
			pStatement.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Get Users Portfolio
	 */
	public List<String> getUserPortfolio(String userid){
		if (conn == null) {
			System.out.println("DBConnection is NULL");
			return new ArrayList<>();
		}
		
		try {
			List<String> res = new ArrayList<>();
			String sql = "SELECT symbol from stocks WHERE user_id = ?";
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setString(1, userid);
			ResultSet rs = pStatement.executeQuery();
			while (rs.next()) {
				String symbol = rs.getString("symbol");
				res.add(symbol);
			}
			
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ArrayList<>();
	}
	
	/*
	 * Get Users Balance
	 */
	public double getUserBalance(String userid){
		if (conn == null) {
			System.out.println("DBConnection is NULL");
			return 0.0;
		}
		
		try {
			String sql = "SELECT  from users WHERE user_id = ?";
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setString(1, userid);
			ResultSet rs = pStatement.executeQuery();
			return rs.getDouble("balance");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	/*
	 * Get Users position at a specific stock
	 */
	public double getPosition(String userid, String symbol) {
		if (conn == null) {
			System.out.println("DBConnection is NULL");
			return 0;
		}
		
		try {
			String sql = "SELECT position from stocks WHERE user_id = ? AND symbol = ?";
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setString(1, userid);
			pStatement.setString(2, symbol);
			System.out.println(pStatement.toString());
			ResultSet rs = pStatement.executeQuery();
			if (rs.next())
			{return rs.getDouble("position");}
			else {
				return 0.0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	/*
	 * Update done to when users submit a buy or sell request
	 */
	public void submitAction(String userid, String action, double amount, String symbol,double price) {
		if (conn == null) {
			System.out.println("DBConnection is NULL");
			return;
		}
		
		try {
			double position = getPosition(userid, symbol);
			action_id ++ ;
			/*
			 * First Set up the logs
			 */
			String sql = "INSERT INTO log(action_id,user_id,symbol,action,action_vol,price)" + 
						"VALUES(?,?,?,?,?,?);";
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setString(1, Integer.toString(action_id));
			pStatement.setString(2, userid);
			pStatement.setString(3, symbol);
			pStatement.setString(4, action);
			pStatement.setDouble(5, amount);
			pStatement.setDouble(6, price);
			pStatement.execute();
			/*
			 * Then Update position
			 */
			double newPosition = (action.equals("buy")? (position + amount) : (position - amount));
			if (newPosition != 0) {
				sql = "INSERT INTO stocks (user_id, symbol, position) VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE position = ?;";
				pStatement = conn.prepareStatement(sql);
				pStatement.setString(1, userid);
				pStatement.setString(2, symbol);
				pStatement.setDouble(3, newPosition);
				pStatement.setDouble(4, newPosition);
				pStatement.execute();
			}
			else {
				sql = "DELETE FROM stocks WHERE userid = ? AND symbol = ?";
				pStatement.setString(1, userid);
				pStatement.setString(2, symbol);
				pStatement.execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/*
	 * Update done when stock price is updated 
	 */
	
	public void updateBalance(String userid) {
		if (conn == null) {
			System.out.println("DBConnection is NULL");
			return;
		}
		AlphaVantageAPI api = new AlphaVantageAPI();
		double newBalance = 0.0;
		//Get the users portfolio of stocks
		List<String> portfolio = getUserPortfolio(userid);
		
		for (String asset : portfolio) {
			//Get all the newest price from the api and calculate the new balance of users
			List<Item> records = api.getItems(api.getResponse(asset));
			int len = records.size();
			double newestPrice = (records.get(len-1).getOpen() + records.get(len-1).getClose())/2.0;
			double position = getPosition(userid, asset);
			newBalance += newestPrice * position;
		}
		
		//Update the balance of users in the table
		try {
			String sql = "UPDATE users SET balance = ? WHERE userid = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(2, userid);
			statement.setDouble(1, newBalance);
			statement.execute();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/*
	 * Add Balance through external injection
	 */
	public void AddBalance(String userid, double amount) {
		try {
			String sql = "UPDATE users SET balance = balance + ? WHERE user_id = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(2, userid);
			statement.setDouble(1, amount);
			statement.execute();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean Verify(String userid, String password) {
		if (conn == null) {
			System.out.println("DBConnection is NULL");
			return false;
		}
		try {
			String sql = "SELECT user_id FROM users where user_id = ? AND password = ?";
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setString(1, userid);
			pStatement.setString(2, password);
			ResultSet res = pStatement.executeQuery();
			while(res.next()) {
				return true;
			}
			return false;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/*
	 * MAIN
	 */
	public static void main(String[] args) {
		MySQLConnection conn = new MySQLConnection();
		for (int i=0; i < DataMock.MOCK_USER_NAMES.length; i++) {
			conn.addUser(DataMock.MOCK_USER_NAMES[i], DataMock.MOCK_PASSWORDS[i], DataMock.MOCK_FIRST_NAMES[i], DataMock.MOCK_LAST_NAMES[i]);
		}
		/*
		 * ========= Actions=================
		 */
		
		/*
		 * LST
		 */
		for (int i = 0; i < DataMock.MOCK_LST_ACTIONS.length; i++) {
			conn.submitAction(DataMock.MOCK_USER_NAMES[0], DataMock.MOCK_LST_ACTIONS[i], DataMock.MOCK_LST_AMOUNTS[i], DataMock.MOCK_LST_ACTION_SYMBOLS[i], DataMock.MOCK_PRICE);
		}
		/*
		 * WYY
		 */
		for (int i = 0; i < DataMock.MOCK_WYY_ACTIONS.length; i++) {
			conn.submitAction(DataMock.MOCK_USER_NAMES[1], DataMock.MOCK_WYY_ACTIONS[i], DataMock.MOCK_WYY_AMOUNTS[i], DataMock.MOCK_WYY_ACTION_SYMBOLS[i], DataMock.MOCK_PRICE);
		}
		/*
		 * AWD
		 */
		for (int i = 0; i < DataMock.MOCK_AWD_ACTIONS.length; i++) {
			conn.submitAction(DataMock.MOCK_USER_NAMES[2], DataMock.MOCK_AWD_ACTIONS[i], DataMock.MOCK_AWD_AMOUNTS[i], DataMock.MOCK_AWD_ACTION_SYMBOLS[i], DataMock.MOCK_PRICE);
		}
		/*
		 * CXY
		 */
		for (int i = 0; i < DataMock.MOCK_CXY_ACTIONS.length; i++) {
			conn.submitAction(DataMock.MOCK_USER_NAMES[3], DataMock.MOCK_CXY_ACTIONS[i], DataMock.MOCK_CXY_AMOUNTS[i], DataMock.MOCK_CXY_ACTION_SYMBOLS[i], DataMock.MOCK_PRICE);
		}
		/*
		 * YZY
		 */
		for (int i = 0; i < DataMock.MOCK_YZY_ACTIONS.length; i++) {
			conn.submitAction(DataMock.MOCK_USER_NAMES[4], DataMock.MOCK_YZY_ACTIONS[i], DataMock.MOCK_YZY_AMOUNTS[i], DataMock.MOCK_YZY_ACTION_SYMBOLS[i], DataMock.MOCK_PRICE);
		}
		/*
		 * ZSJ
		 */
		for (int i = 0; i < DataMock.MOCK_ZSJ_ACTIONS.length; i++) {
			conn.submitAction(DataMock.MOCK_USER_NAMES[5], DataMock.MOCK_ZSJ_ACTIONS[i], DataMock.MOCK_ZSJ_AMOUNTS[i], DataMock.MOCK_ZSJ_ACTION_SYMBOLS[i], DataMock.MOCK_PRICE);
		}
		
		/**
		 * Add Balance
		 */
		conn.AddBalance(DataMock.MOCK_USER_NAMES[1], 1000);
		conn.close();
		
	}
}
