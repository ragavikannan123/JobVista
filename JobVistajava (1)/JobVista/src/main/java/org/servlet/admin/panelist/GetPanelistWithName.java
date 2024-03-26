package org.servlet.admin.panelist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

import org.util.CommonLogger;
import org.recruitment.organization.AdminManagement;

@WebServlet("/GetPanelistWithName")
public class GetPanelistWithName extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    Logger logger = CommonLogger.getCommon().getLogger(GetPanelistWithName.class);
    
    public GetPanelistWithName() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String line;
        JSONObject responseData = new JSONObject();
        AdminManagement adminManagement = new AdminManagement();
       
        int adminId = 0;
        
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        
        try {
        	
            JSONObject jsonObject = new JSONObject(sb.toString());
            int panelistId = jsonObject.getInt("panelistId");
            
            JSONObject userDetails = jsonObject.getJSONObject("userDetails");
			adminId = userDetails.getInt("Admin_Id");
		
            JSONArray panelistData = adminManagement.getPanelistWithName(panelistId);
            responseData.put("statusCode", 200);
            responseData.put("message",panelistData);
            
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
	    	logger.error("Admin:"+adminId+"\nError occurred while retrieving data from the database. \n"+e.getMessage());
	        
	    }
	    
	    response.getWriter().write(responseData.toString());
    
    }
    
}
