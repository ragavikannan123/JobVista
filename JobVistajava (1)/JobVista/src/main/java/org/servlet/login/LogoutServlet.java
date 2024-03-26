package org.servlet.login;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.util.CommonLogger;

/**
 * Servlet implementation class LogoutServlet
 */
@WebServlet("/Logout")

public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Logger logger = CommonLogger.getCommon().getLogger(LogoutServlet.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogoutServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		JSONObject responseObject = new JSONObject();
		Cookie[] cookies = request.getCookies();
	    if (cookies != null) {
	    	
	        for (Cookie cookie : cookies) {
	        	if(cookie.getName().equals("admin_Id")) {
	        		logger.info("Admin "+cookie.getValue()+" was logged out");
	        	}
	        	if(cookie.getName().equals("panelist_Id")) {
	        		logger.info("Panelist "+cookie.getValue()+" was logged out");
	        	}
	            cookie.setMaxAge(0);
	            response.addCookie(cookie);
	        }
	        
	        try {
				responseObject.put("statusCode", 200);
				responseObject.put("message", "You have logged out");
			} 
	        catch (JSONException e) {
				logger.error("Error parsing JSON object. "+e.getMessage());
			}
	        
	    }
	  
	    response.getWriter().write(responseObject.toString());
	    
	}

}
