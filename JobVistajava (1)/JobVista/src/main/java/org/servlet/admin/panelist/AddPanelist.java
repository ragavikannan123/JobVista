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
import org.json.JSONException;
import org.json.JSONObject;

import org.util.CommonLogger;
import org.util.Gender;
import org.recruitment.organization.AdminManagement;

/**
 * Servlet implementation class AddPanelist
 */
@WebServlet("/AddPanelist")
public class AddPanelist extends HttpServlet {
	
	Logger logger = CommonLogger.getCommon().getLogger(AddPanelist.class);
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddPanelist() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);
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
		response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		
		System.out.println("get");
        JSONObject responseData = new JSONObject();
        AdminManagement adminManagement = new AdminManagement();
         
        int orgId = 0;
        int adminId = 0;

//        Cookie[] cookies = request.getCookies();
//		String orgId = "1";
//		String adminId = null;
		
//        if (cookies != null) {
//            
//        	for(Cookie cookie:cookies) {
//        		if(cookie.getName().equalsIgnoreCase("org_Id")) {
//        			orgId = cookie.getValue();
//            	}
//        		if(cookie.getName().equalsIgnoreCase("admin_Id")) {
//        			adminId = cookie.getValue();
//        		}
//        	}
//        }
        
        try {
            
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String line;
            
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            
            JSONObject jsonObject = new JSONObject(sb.toString());
            
            JSONObject userDetails = jsonObject.getJSONObject("userDetails");
			orgId = userDetails.getInt("Org_Id");
			adminId = userDetails.getInt("Admin_Id");
			
			System.out.println(jsonObject.toString());
			
			JSONObject panelist = jsonObject.getJSONObject("panelist");
            String name = panelist.getString("name");
            String email = panelist.getString("email");
            String genderStr = panelist.getString("gender");
            System.out.println(genderStr);
            Gender  gender = Gender.valueOf(genderStr.toUpperCase());
            String position = panelist.getString("position");
            String password = panelist.getString("password");
            String department = panelist.getString("department");
            responseData.put("statusCode", 200);
            responseData.put("message",adminManagement.addPanelist(name, email, gender, position, password, department, orgId));
        
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
