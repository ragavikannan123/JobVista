package org.servlet.login;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.recruitment.login.Login;
import org.util.CommonLogger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
	
	Logger logger = CommonLogger.getCommon().getLogger(LoginServlet.class);
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public LoginServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setStatus(HttpServletResponse.SC_OK);
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        
        
		System.out.println("get servlet");
		JSONObject jsonObject = (JSONObject) request.getAttribute("object");
		
		String email = null;
		String password = null;
		
		try {
			
			email = jsonObject.getString("email");
			password = jsonObject.getString("password");
		
		} 
		
		catch (JSONException e) {
		
			logger.error("json exception in signup servlet while getting data from json object \n"+e.getMessage());
		
		}
		
		Login login = new Login();
		String output = null;
		JSONObject responseObject = new JSONObject();
		
		try {
			output = login.checkUser(email, password);
			
			if(output.equals("Invalid email or password")) {
				responseObject.put("statusCode", 500);
				responseObject.put("message", output);
			}
			else {
				JSONObject detailsObject = login.updateSession(email);

				responseObject.put("statusCode", 200);
				responseObject.put("message", "Logged in successfully");
				responseObject.put("userDetails", detailsObject);
			}
		
		}
		
		catch (JSONException e) {
	        
	    	logger.error("Error parsing JSON object. "+e.getMessage());
	    	try {
				responseObject.put("statusCode", 500);
				responseObject.put("message", "Error parsing JSON object.");
			} 
	    	catch (JSONException e1) {
	    		logger.error("Error parsing JSON object. "+e1.getMessage());
			}
	        
	    } 
	    catch (SQLException e) {
	      
	    	try {
	    		responseObject.put("statusCode", 500);
	    		responseObject.put("message", "Error occurred while retrieving data from the database.");
			} 
	    	catch (JSONException e1) {
	    		logger.error("Error parsing JSON object. "+e1.getMessage());
			}
	    	logger.error("Error occurred while retrieving data from the database. "+e.getMessage());
	       
	    }
	    
	    
		System.out.println("responce: "+responseObject.toString());
	    response.getWriter().write(responseObject.toString());
		
	}

}
