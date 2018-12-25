package rpc;

import java.util.*;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import external.AlphaVantageAPI;
import entity.Item;
/**
 * Servlet implementation class Price
 */
@WebServlet("/price")
public class Price extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Price() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Hello");
		try {
			JSONArray arr = new JSONArray();
			AlphaVantageAPI api = new AlphaVantageAPI();
			Map<String,String[]> assets = request.getParameterMap();
			for (Map.Entry<String, String[]> entry : assets.entrySet()) {
				String asset = entry.getValue()[0];
				System.out.println(asset);
				List<Item> items = api.getItems(api.getResponse(asset));
				JSONObject obj = new JSONObject();
				if (items.size() > 0) {
					double price = items.get(items.size()-1).getOpen();
					double initPrice = items.get(0).getOpen();
		   			obj.put("asset",asset).put("price", Double.toString(price));
		   			double diff = price-initPrice;
		   			if (Math.abs(diff) < 0.001) {
		   				obj.put("trend", "neutral");
		   			}
		   			else if (diff > 0) {
		   				obj.put("trend", "up");
		   			}
		   			else if (diff < 0) {
		   				obj.put("trend", "down");
		   			}
		   			arr.put(obj);
		   			
				}
				else {
					obj.put("asset", asset).put("price", "N/A").put("trend", "none");
					arr.put(obj);
				}
			}

   			RpcHelper.writeJsonArray(response, arr);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
