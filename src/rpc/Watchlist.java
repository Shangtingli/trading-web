package rpc;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import db.MySQLConnection;

/**
 * Servlet implementation class Watchlist
 */
@WebServlet("/watchlist")
public class Watchlist extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Watchlist() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		MySQLConnection conn = new MySQLConnection();
		JSONObject obj = new JSONObject();
		try {
			String userid = request.getParameter("userid");
			List<String> watchlist = conn.getWatchList(userid);
			for (int i=0; i < watchlist.size(); i++) {
				obj.put("asset"+Integer.toString(i), watchlist.get(i));
			}
			RpcHelper.writeJsonObject(response, obj);
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
		// TODO Auto-generated method stub
		MySQLConnection conn = new MySQLConnection();
		JSONObject obj = new JSONObject();
		try {
			String method = request.getParameter("method");
			String userid = request.getParameter("userid");
   			String symbol = request.getParameter("symbol");
   			if (method.equals("add")) {
	   			if (conn.isExistedWatchList(userid, symbol)) {
	   				obj.put("result","failed");
	   			}
	   			else {
	   				conn.setWatchList(userid, symbol);
	   				obj.put("result","success");
	   			}
	   			RpcHelper.writeJsonObject(response, obj);
   			}
   			else if (method.equals("remove")) {
   				conn.unSetWatchList(userid, symbol);
   				obj.put("result","success");
   				RpcHelper.writeJsonObject(response, obj);
   			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			conn.close();
		}
	}

}
