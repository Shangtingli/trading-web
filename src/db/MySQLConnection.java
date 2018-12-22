package db;
	
import java.sql.*;
import java.util.*;
import db.MySQLDBUtil;
import db.DataMock;
import entity.Item;
import external.AlphaVantageAPI;

public class MySQLConnection {
	private Connection conn;
	
	//Constructor for MySQLConnection
	public MySQLConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
			conn = DriverManager.getConnection(MySQLDBUtil.URL);
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
	 * Register a new user
	 */
	public void addUser(String userid, String password, String firstname, String lastname) {
		if (conn == null) {
			System.out.println("DBConnection is NULL");
			return;
		}
		
		try {
			String sql = "INSERT IGNORE INTO users(user_id,password,first_name,last_name,balance) VALUES(?,?,?,?,0)";
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setString(1, userid);
			pStatement.setString(2, password);
			pStatement.setString(3, firstname);
			pStatement.setString(4, lastname);
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
			String sql = "SELECT position from users WHERE user_id = ? AND symbol = ?";
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setString(1, userid);
			pStatement.setString(2, symbol);
			ResultSet rs = pStatement.executeQuery();
			return rs.getDouble("position");
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
			/*
			 * First Set up the logs
			 */
			String sql = "INSERT IGNORE INTO log(user_id,symbol,action,action_vol,price)" + 
						"VALUES(?,?,?,?,?);";
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setString(1, userid);
			pStatement.setString(2, symbol);
			pStatement.setString(3, action);
			pStatement.setDouble(4, amount);
			pStatement.setDouble(5, price);
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
	public static void main(String[] args) {
		MySQLConnection conn = new MySQLConnection();
		
	}
}
