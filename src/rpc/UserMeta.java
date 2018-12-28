package rpc;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import db.MySQLConnection;

/**
 * Servlet implementation class Holdings
 */
@WebServlet("/usermeta")
public class UserMeta extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserMeta() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		MySQLConnection conn = new MySQLConnection();
		JSONArray array = new JSONArray();
		JSONObject obj1 = new JSONObject();
		JSONObject obj2 = new JSONObject();
		try {
			
			String userid = request.getParameter("userid");
			Map<String,Double> holdings = conn.getHoldings(userid);
			Map<String,Double> usermeta = conn.getUserMeta(userid);
			for (Map.Entry<String, Double> entry : holdings.entrySet()) {
				double value = (double)Math.round(entry.getValue() * 100d) / 100d;
				obj1.put(entry.getKey(),Double.toString(value));
			}
			
			for (Map.Entry<String, Double> entry : usermeta.entrySet()) {
				String key = "";
				if (entry.getKey().equals("total_value")) {
					key = "Total";
				}
				else if (entry.getKey().equals("balance")) {
					key = "Principal Capital";
				}
				else if (entry.getKey().equals("asset_value")) {
					key = "Portfolio Value";
				}
				double value = (double)Math.round(entry.getValue() * 100d) / 100d;
				obj2.put(key, Double.toString(value));
			}
			System.out.println(usermeta);
			array.put(obj1);
			array.put(obj2);
			RpcHelper.writeJsonArray(response, array);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			conn.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MySQLConnection conn = new MySQLConnection();
		Double amount = Double.parseDouble(request.getParameter("amount"));
		String method = request.getParameter("method");
		String userid = request.getParameter("userid");
		JSONObject res = new JSONObject();
		try {
			if (method.equals("add")) {
				conn.AddBalance(userid, amount);
				res.put("result", "success");
			}
			else if (method.equals("remove")) {
				double currentBalance = conn.getUserMeta(userid).get("balance");
				System.out.println("currentBalance");
				if (currentBalance < amount) {
					res.put("result", "failure");
					RpcHelper.writeJsonObject(response, res);
					return;
				}
				conn.RemoveBalance(userid, amount);
				res.put("result", "success");
			}
			
			RpcHelper.writeJsonObject(response, res);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			conn.close();
		}
		
	}

}
