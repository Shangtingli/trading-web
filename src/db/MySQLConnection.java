package db;
	
import java.sql.*;
import java.util.*;

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
	 * Find Holdings for the user
	 */
	public Map<String,Double> getHoldings(String userid) {
		Map<String,Double> res = new HashMap<>();
		if (conn == null) {
			System.out.println("DBConnection is NULL");
			return res;
		}
		
		try {
			String sql = "SELECT symbol,position FROM stocks WHERE user_id = ?";
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setString(1, userid);
			ResultSet rs = pStatement.executeQuery();
			while(rs.next()) {
				res.put(rs.getString("symbol"),rs.getDouble("position"));
			}
			
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return res;
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
			pStatement.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public Map<String, Double> getUserMeta(String userid) {
		Map<String, Double> res = new HashMap<>();
		if (conn == null) {
			System.out.println("DBConnection is NULL");
			return res;
		}
		
		try {
			String sql = "SELECT balance, asset_value, total_value from users WHERE user_id = ?";
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setString(1, userid);
			System.out.println(pStatement.toString());
			ResultSet rs = pStatement.executeQuery();
			rs.next();
			res.put("balance", rs.getDouble("balance"));
			res.put("asset_value", rs.getDouble("asset_value"));
			res.put("total_value", rs.getDouble("total_value"));
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return res;
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
	public JSONObject submitAction(String userid, String action, double amount, String symbol) {
		JSONObject res = new JSONObject();
		if (conn == null) {
			System.out.println("DBConnection is NULL");
			return res;
		}
		
		AlphaVantageAPI api = new AlphaVantageAPI();
		try {
			double position = getPosition(userid, symbol);
			action_id ++ ;
			JSONObject response = api.getResponse(symbol);

			if (response.length() ==0) {
				System.out.println("The Action you sent does not work, either because of API Restriction or the symbol does not exist");
				res.put("result", "failure").put("reason","symbol");
				return res;
			}
			
			List<Item> records = api.getItems(response); 
			int len = records.size();
			double price = records.get(len-1).getOpen();
			double balanceChanged = (action.equals("buy")) ? (price * amount):(- price * amount);
			Map<String, Double> metadata = getUserMeta(userid);
			double total_value = metadata.get("total_value");
			double capital = metadata.get("balance");
			if (balanceChanged > Math.min(total_value, capital)) {
				res.put("result", "failure").put("reason","amount");
				return res;
			}
			/*
			 * First Set up the logs
			 */
			String sql = "INSERT INTO log(action_id,user_id,symbol,action,action_vol,price) " + 
						"VALUES(?,?,?,?,?,?);";
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setString(1, Integer.toString(action_id));
			pStatement.setString(2, userid);
			pStatement.setString(3, symbol);
			pStatement.setString(4, action);
			pStatement.setDouble(5, amount);
			pStatement.setDouble(6, price);
			System.out.println("Set up Logs Statement");
			System.out.println(pStatement.toString());
			pStatement.execute();
			/*
			 * Then Update position
			 */
			double newPosition = (action.equals("buy")? (position + amount) : (position - amount));
			if (newPosition != 0) {
				sql = "INSERT INTO stocks (user_id, symbol, position) VALUES(?, ?, ?) ON DUPLICATE KEY UPDATE position = ?";
				PreparedStatement pStatement1 = conn.prepareStatement(sql);
				pStatement1.setString(1, userid);
				pStatement1.setString(2, symbol);
				pStatement1.setDouble(3, newPosition);
				pStatement1.setDouble(4, newPosition);
				System.out.println("New Position is not 0 Statement");
				System.out.println(pStatement1.toString());
				pStatement1.execute();
			}
			else {
				sql = "DELETE FROM stocks WHERE user_id = ? AND symbol = ?";
				PreparedStatement pStatement2 = conn.prepareStatement(sql);
				pStatement2.setString(1, userid);
				pStatement2.setString(2, symbol);
				System.out.println("Delete From Stocks Statement");
				System.out.println(pStatement2.toString());
				pStatement2.execute();
			}
			
			/*
			 * Then Update Balance and Asset value
			 */
			if (balanceChanged > 0) {
				sql = "UPDATE users SET balance = balance - ?, asset_value = asset_value + ? WHERE user_id = ?";
			}
			else {
				sql = "UPDATE users SET balance = balance + ?, asset_value = asset_value - ? WHERE user_id = ?";
			}
			pStatement = conn.prepareStatement(sql);
			pStatement.setDouble(1, Math.abs(balanceChanged));
			pStatement.setDouble(2, Math.abs(balanceChanged));
			pStatement.setString(3, userid);
			System.out.println("Update Balance Statement");
			System.out.println(pStatement.toString());
			pStatement.execute();
			res.put("result", "success");
			return res;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	
	/*
	 * Update done when stock price is updated 
	 */
	
	public void updateAssetBalance(String userid) {
		if (conn == null) {
			System.out.println("DBConnection is NULL");
			return;
		}
		AlphaVantageAPI api = new AlphaVantageAPI();
		double asset_value = 0.0;
		//Get the users portfolio of stocks
		List<String> portfolio = new ArrayList<>(getHoldings(userid).keySet());
		
		for (String asset : portfolio) {
			//Get all the newest price from the api and calculate the new balance of users
			List<Item> records = api.getItems(api.getResponse(asset));
			if (records.size () ==0) {
				System.out.println("The Action you sent does not work because of fucking API Restrictions");
				return;
			}
			int len = records.size();
			double newestPrice = records.get(len-1).getOpen();
			double position = getPosition(userid, asset);
			asset_value += newestPrice * position;
		}
		
		
		try {
			//Update the asset_value of user in the table
			String sql = "UPDATE users SET asset_value = ?, total_value = ? + balance WHERE userid = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(3, userid);
			statement.setDouble(1, asset_value);
			statement.setDouble(2, asset_value);
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
			String sql = "UPDATE users SET balance = balance + ?, total_value = total_value + ? WHERE user_id = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(3, userid);
			statement.setDouble(1, amount);
			statement.setDouble(2, amount);
			statement.execute();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void RemoveBalance(String userid, double amount) {
		try {
			String sql = "UPDATE users SET balance = balance - ?, total_value = total_value - ? WHERE user_id = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(3, userid);
			statement.setDouble(1, amount);
			statement.setDouble(2, amount);
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
	 * Get Users WatchList
	 */
	public List<String> getWatchList(String userid) {
		List<String> arr = new ArrayList<>();
		if (conn == null) {
			System.out.println("DBConnection is NULL");
			return arr;
		}
		try {
			String sql = "SELECT symbol FROM watchlists where user_id = ?";
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setString(1, userid);
			ResultSet res = pStatement.executeQuery();
			while(res.next()) {
				String symbol = res.getString("symbol");
				arr.add(symbol);
			}
			return arr;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return arr;
	}
	/*
	 * Set the symbol in user's watchLists
	 */
	public void setWatchList(String userid,String symbol) {
		if (conn == null) {
			System.out.println("DBConnection is NULL");
			return;
		}
		try {
			String sql = "INSERT INTO watchlists(user_id,symbol) VALUES(?,?)";
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setString(1, userid);
			pStatement.setString(2, symbol);
			pStatement.execute();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * Check to see if the user already have the ticker on watchlist
	 */
	public boolean isExistedWatchList(String userid, String symbol) {
		if (conn == null) {
			System.out.println("DBConnection is NULL");
			return false;
		}
		try {
			String sql = "SELECT COUNT(*) AS count FROM watchlists WHERE user_id = ? AND symbol = ?";
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setString(1, userid);
			pStatement.setString(2, symbol);
			System.out.println(pStatement.toString());
			ResultSet rs = pStatement.executeQuery();
			while(rs.next()) {
				return (rs.getInt("count") > 0); 
					
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/*
	 * UnSet the symbol in user's watchLists
	 */
	public void unSetWatchList(String userid,String symbol) {
		if (conn == null) {
			System.out.println("DBConnection is NULL");
			return;
		}
		try {
			String sql = "DELETE FROM watchlists WHERE user_id = ? AND symbol = ?";
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setString(1, userid);
			pStatement.setString(2, symbol);
			pStatement.execute();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	/*
	 * MAIN
	 */
	public static void main(String[] args) {
		MySQLConnection conn = new MySQLConnection();
		for (int i=0; i < DataMock.MOCK_USER_NAMES.length; i++) {
			conn.addUser(DataMock.MOCK_USER_NAMES[i], DataMock.MOCK_PASSWORDS[i], DataMock.MOCK_FIRST_NAMES[i], DataMock.MOCK_LAST_NAMES[i]);
			conn.AddBalance(DataMock.MOCK_USER_NAMES[i], 20000);
		}
		/*
		 * ========= Actions=================
		 */
		
		/*
		 * LST
		 */
		for (int i = 0; i < DataMock.MOCK_LST_ACTIONS.length; i++) {
			conn.submitAction(DataMock.MOCK_USER_NAMES[0], DataMock.MOCK_LST_ACTIONS[i], DataMock.MOCK_LST_AMOUNTS[i], DataMock.MOCK_LST_ACTION_SYMBOLS[i]);
		}
		
		for (int i = 0; i < DataMock.MOCK_LST_WATCHLIST.length; i++) {
			conn.setWatchList(DataMock.MOCK_USER_NAMES[0], DataMock.MOCK_LST_WATCHLIST[i]);
		}
		
		
		/*
		 * WYY
		 */
		for (int i = 0; i < DataMock.MOCK_WYY_ACTIONS.length; i++) {
			conn.submitAction(DataMock.MOCK_USER_NAMES[1], DataMock.MOCK_WYY_ACTIONS[i], DataMock.MOCK_WYY_AMOUNTS[i], DataMock.MOCK_WYY_ACTION_SYMBOLS[i]);
		}
		
		for (int i = 0; i < DataMock.MOCK_WYY_WATCHLIST.length; i++) {
			conn.setWatchList(DataMock.MOCK_USER_NAMES[1], DataMock.MOCK_WYY_WATCHLIST[i]);
		}
		/*
		 * AWD
		 */
		for (int i = 0; i < DataMock.MOCK_AWD_ACTIONS.length; i++) {
			conn.submitAction(DataMock.MOCK_USER_NAMES[2], DataMock.MOCK_AWD_ACTIONS[i], DataMock.MOCK_AWD_AMOUNTS[i], DataMock.MOCK_AWD_ACTION_SYMBOLS[i]);
		}
		
		for (int i = 0; i < DataMock.MOCK_AWD_WATCHLIST.length; i++) {
			conn.setWatchList(DataMock.MOCK_USER_NAMES[2], DataMock.MOCK_AWD_WATCHLIST[i]);
		}
		/*
		 * CXY
		 */
		for (int i = 0; i < DataMock.MOCK_CXY_ACTIONS.length; i++) {
			conn.submitAction(DataMock.MOCK_USER_NAMES[3], DataMock.MOCK_CXY_ACTIONS[i], DataMock.MOCK_CXY_AMOUNTS[i], DataMock.MOCK_CXY_ACTION_SYMBOLS[i]);
		}
		
		for (int i = 0; i < DataMock.MOCK_CXY_WATCHLIST.length; i++) {
			conn.setWatchList(DataMock.MOCK_USER_NAMES[3], DataMock.MOCK_CXY_WATCHLIST[i]);
		}
		/*
		 * YZY
		 */
		for (int i = 0; i < DataMock.MOCK_YZY_ACTIONS.length; i++) {
			conn.submitAction(DataMock.MOCK_USER_NAMES[4], DataMock.MOCK_YZY_ACTIONS[i], DataMock.MOCK_YZY_AMOUNTS[i], DataMock.MOCK_YZY_ACTION_SYMBOLS[i]);
		}
		
		for (int i = 0; i < DataMock.MOCK_YZY_WATCHLIST.length; i++) {
			conn.setWatchList(DataMock.MOCK_USER_NAMES[4], DataMock.MOCK_YZY_WATCHLIST[i]);
		}
		/*
		 * ZSJ
		 */
		for (int i = 0; i < DataMock.MOCK_ZSJ_ACTIONS.length; i++) {
			conn.submitAction(DataMock.MOCK_USER_NAMES[5], DataMock.MOCK_ZSJ_ACTIONS[i], DataMock.MOCK_ZSJ_AMOUNTS[i], DataMock.MOCK_ZSJ_ACTION_SYMBOLS[i]);
		}
		
		for (int i = 0; i < DataMock.MOCK_ZSJ_WATCHLIST.length; i++) {
			conn.setWatchList(DataMock.MOCK_USER_NAMES[5], DataMock.MOCK_ZSJ_WATCHLIST[i]);
		}
		
		/**
		 * Add Balance
		 */
		conn.close();
		
	}
}
