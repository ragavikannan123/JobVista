package org.servlet.admin.opening;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.recruitment.organization.AdminManagement;
import org.util.CommonLogger;


@WebServlet("/GetCurrentOpenings")
public class GetCurrentOpenings extends HttpServlet {

	Logger logger = CommonLogger.getCommon().getLogger(GetCurrentOpenings.class);
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetCurrentOpenings() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doOptions(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		response.setStatus(HttpServletResponse.SC_OK);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");

		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    
	    JSONObject responseData = new JSONObject();
		AdminManagement adminManagement = new AdminManagement();
		StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
		
		int orgId = 0;
		int adminId = 0;
		try {
			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONObject userDetails = jsonObject.getJSONObject("userDetails");
			orgId = userDetails.getInt("Org_Id");
			adminId = userDetails.getInt("Admin_Id");
		
		} 
		
		catch (JSONException e) {
		
			logger.error("json exception while getting data from json object \n"+e.getMessage());
		
		}
		
        
        try {
        	JSONArray openingsData = adminManagement.getCurrentOpenings(orgId);
        	
        	responseData.put("statusCode", 200);
			responseData.put("message", openingsData);
		} 
        catch (JSONException e) {
        	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	    	logger.error("User:"+adminId+"\nError parsing JSON object.\n" + e.getMessage());
	    	try {
				responseData.put("statusCode", 500);
				responseData.put("message", "Error parsing JSON object.\n");
			} 
	    	catch (JSONException e1) {
	    		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	    		logger.error("User:"+adminId+"\nError parsing JSON object." + e1.getMessage());
			}
	        
	    } 
	    catch (SQLException e) {
	    	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	    	e.printStackTrace();
	    	try {
				responseData.put("statusCode", 500);
				responseData.put("message", "Error occurred while retrieving data from the database.");
			} 
	    	catch (JSONException e1) {
	    		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	    		logger.error("User: "+adminId+"\nError parsing JSON object.\n" + e1.getMessage());
			}
	    	logger.error("User:"+adminId+"\nError occurred while retrieving data from the database. \n"+e.getMessage());
	        
	    }
	    
        System.out.println(responseData.toString());
	    response.getWriter().write(responseData.toString());
	        
	}
}
