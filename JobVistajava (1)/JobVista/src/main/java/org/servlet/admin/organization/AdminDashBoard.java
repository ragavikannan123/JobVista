package org.servlet.admin.organization;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.recruitment.organization.AdminManagement;
import org.servlet.admin.opening.GetCurrentOpenings;
import org.util.CommonLogger;

/**
 * Servlet implementation class AdminDashBoard
 */
@WebServlet("/AdminDashBoard")
public class AdminDashBoard extends HttpServlet {
	
	Logger logger = CommonLogger.getCommon().getLogger(GetCurrentOpenings.class);
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpS // obj:  {
        //     admin: {},
        //     organization: {}
        // }ervlet()
     */
    public AdminDashBoard() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        
        doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		
		response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        
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
        
        	JSONArray departments = adminManagement.getDepartments(orgId);
        	JSONObject jsonResponse = new JSONObject();
        	JSONArray departmentArray = new JSONArray();
        	for (int i = 0; i < departments.length(); i++) {
        	    JSONObject department = departments.getJSONObject(i);
        	    int departmentId = department.getInt("id");
        	    JSONArray openings = adminManagement.getOpeningsWithDepartment(departmentId);
        	    JSONObject departmentObject = new JSONObject(department.toString());
        	    JSONArray openingArray = new JSONArray();
        	    for (int j = 0; j < openings.length(); j++) {
        	        JSONObject opening = openings.getJSONObject(j);
        	        int openingId = opening.getInt("id");
        	        JSONArray applicants = adminManagement.seeApplicants(openingId, "Selected");
        	        JSONObject openingObject = new JSONObject(opening.toString());
        	        openingObject.put("applicants", applicants);
        	        openingArray.put(openingObject);
        	    }
        	    
        	    departmentObject.put("openings", openingArray);
        	    departmentArray.put(departmentObject);
        	}

        	jsonResponse.put("departments", departmentArray);
        	responseData.put("statusCode", 200);
        	responseData.put("message", jsonResponse);

		
        }
        
        catch (JSONException e) {
        	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
	    	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
