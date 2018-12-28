package rpc;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.security.auth.spi.LoginModule;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import db.MySQLConnection;
import entity.*;
/**
 * Servlet implementation class Chart
 */
@WebServlet("/chart")
public class Chart extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Chart() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userid = request.getParameter("userid");
		if (userid.length() == 0) {
			noLoginMode(request, response);
		}
		else {
			LoginMode(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private void noLoginMode(HttpServletRequest request, HttpServletResponse response) {
		MySQLConnection conn = new MySQLConnection();
		Map<String, String[]> map =request.getParameterMap();
		int interval = Integer.parseInt(map.get("interval")[0]);
		int length = Integer.parseInt(map.get("length")[0]);
		try {
			JSONArray array = new JSONArray();
			for (Map.Entry<String, String[]> entry : map.entrySet()) {
				if (entry.getKey().equals("interval") || entry.getKey().equals("userid") || entry.getKey().equals("length")) {
					continue;
				}
				JSONObject asset_info = new JSONObject();
				String asset = entry.getValue()[0];
				List<Item> itemList = conn.getAllPrices(asset, interval);
				itemList = itemList.subList(itemList.size()-length, itemList.size());
				JSONArray arr = new JSONArray();
				for (Item item : itemList) {
					JSONObject obj = new JSONObject();
					obj.put("time", item.getTime());
					obj.put("price", item.getOpen());
					arr.put(obj);
				}
				asset_info.put(asset, arr);
				array.put(asset_info);
			}
			RpcHelper.writeJsonArray(response, array);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			conn.close();
		}
	}
	
	private void LoginMode(HttpServletRequest request, HttpServletResponse response) {
		MySQLConnection conn = new MySQLConnection();
		String userid= request.getParameter("userid");
		double balance = conn.getUserMeta(userid).get("balance");
		Map<String, Double> map = conn.getHoldings(userid);
		int interval = Integer.parseInt(request.getParameter("interval"));
		int length = Integer.parseInt(request.getParameter("length"));
		try {
			JSONArray array = new JSONArray();
			for (Map.Entry<String, Double> entry : map.entrySet()) {
				JSONObject asset_info = new JSONObject();
				String asset = entry.getKey();
				List<Item> itemList = conn.getAllPrices(asset, interval);
				itemList = itemList.subList(itemList.size()-length, itemList.size());
				JSONArray arr = new JSONArray();
				for (Item item : itemList) {
					JSONObject obj = new JSONObject();
					obj.put("time", item.getTime());
					obj.put("price", item.getOpen());
					arr.put(obj);
				}
				asset_info.put(asset, arr).put("holdings", Double.toString(entry.getValue()));
				array.put(asset_info);
			}
			JSONObject object = new JSONObject();
			object.put("capital",Double.toString(balance));
			array.put(object);
			RpcHelper.writeJsonArray(response, array);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			conn.close();
		}
	}

}
