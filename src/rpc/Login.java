package rpc;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import db.MySQLConnection;
/**
 * Servlet implementation class Login
 */
@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		MySQLConnection conn = new MySQLConnection();
		System.out.println("Hello i am do get method");
		JSONObject obj = new JSONObject();
		try {
			String userid = request.getParameter("userid");
   			String password = request.getParameter("password");
   			if (conn.Verify(userid, password)) {
   				obj.put("result", "success").put("user_id", userid);
   			}
   			else {
   				obj.put("result", "failure");
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
		doGet(request, response);
	}
/*http://localhost:8080/trading-web/login?userid=shangtingli&password=shangtingli*/
}
