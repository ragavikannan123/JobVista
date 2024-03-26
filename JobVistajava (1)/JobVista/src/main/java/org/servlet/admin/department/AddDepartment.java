package org.servlet.admin.department;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.recruitment.organization.AdminManagement;
import org.util.CommonLogger;

/**
 * Servlet implementation class AddDepartment
 */
@WebServlet("/AddDepartment")
public class AddDepartment extends HttpServlet {
	
	Logger logger = CommonLogger.getCommon().getLogger(AddDepartment.class);
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddDepartment() {
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
		response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");
	    JSONObject responseData = new JSONObject();
	    AdminManagement adminManagement = new AdminManagement();
	    
		int orgId = 0; 
		int adminId = 0;

	    try {
	       
	        StringBuilder sb = new StringBuilder();
	        BufferedReader reader = request.getReader();
	        String line;
	        
	        while ((line = reader.readLine()) != null) {
	            sb.append(line);
	        }
	        
	        JSONObject jsonObject = new JSONObject(sb.toString());
	       
	        JSONObject departmentValue = jsonObject.getJSONObject("departmentValue");
	        String title = departmentValue.getString("name");
	        String description = departmentValue.getString("description");
	        JSONObject userDetails = jsonObject.getJSONObject("userDetails");
			orgId = userDetails.getInt("Org_Id");
			adminId = userDetails.getInt("Admin_Id");
	        responseData.put("statusCode", 200);
	        responseData.put("message",adminManagement.addDepartment(title, description, orgId));
	        
	    } 
	    
	    catch (JSONException e) {
	      
	    	logger.error("Admin:"+adminId+"\nError parsing JSON object.\n" + e.getMessage());
	    	try {
				responseData.put("statusCode", 500);
				responseData.put("message", "Error parsing JSON object.\n");
			} 
	    	catch (JSONException e1) {
	    		logger.error("Admin:"+adminId+"\nError parsing JSON object." + e1.getMessage());
			}
	        
	    } 
	    catch (SQLException e) {
	
	    	try {
				responseData.put("statusCode", 500);
				responseData.put("message", "Error occurred while retrieving data from the database.");
			} 
	    	catch (JSONException e1) {
	    		logger.error("Admin: "+adminId+"\nError parsing JSON object.\n" + e1.getMessage());
			}
	    	logger.error("Admin:"+adminId+"\nError occurred while adding data in the database. \n"+e.getMessage());
	        
	    }

	    response.getWriter().write(responseData.toString());
	
	}

}
