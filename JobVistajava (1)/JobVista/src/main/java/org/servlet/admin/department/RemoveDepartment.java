package org.servlet.admin.department;

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
import org.json.JSONException;
import org.json.JSONObject;
import org.recruitment.organization.AdminManagement;
import org.util.CommonLogger;

@WebServlet("/RemoveDepartment")
public class RemoveDepartment extends HttpServlet {
	
	Logger logger = CommonLogger.getCommon().getLogger(RemoveDepartment.class);
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RemoveDepartment() {
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
        
        JSONObject responseData = new JSONObject();
        AdminManagement adminManagement = new AdminManagement();
     
		int adminId = 0;
		
        try {
            
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            
            JSONObject jsonObject = new JSONObject(sb.toString());
            int departmentId = jsonObject.getInt("departmentId");
            JSONObject userDetails = jsonObject.getJSONObject("userDetails");
			adminId = userDetails.getInt("Admin_Id");
            boolean removed = adminManagement.removeDepartments(departmentId);

            if (removed) {
                responseData.put("statusCode", 200);
                responseData.put("message", "Department removed successfully.");
            } 
            else {
                responseData.put("statusCode", 500);
                responseData.put("message", "Failed to remove department.");
            }

            
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
	    	logger.error("Admin:"+adminId+"\nError occurred while removing data in the database. \n"+e.getMessage());
	        
	    }
        response.getWriter().write(responseData.toString());
        
    }
    
}
