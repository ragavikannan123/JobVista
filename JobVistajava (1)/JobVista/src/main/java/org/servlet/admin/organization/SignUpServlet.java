package org.servlet.admin.organization;

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
import org.recruitment.organization.SignUp;
import org.util.CommonLogger;

/**
 * Servlet implementation class SignUpServlet
 */
@WebServlet("/SignUp")
public class SignUpServlet extends HttpServlet {
	Logger logger = CommonLogger.getCommon().getLogger(SignUpServlet.class);
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUpServlet() {
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
//		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @sefile:///home/ragavi-zstk352/Downloads/jakarta.servlet-api-6.1.0-M1.jar
e HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		//JSONObject jsonObject = (JSONObject) request.getAttribute("object");
//		
//		StringBuffer jb = new StringBuffer();
//		String line = null; 
//		BufferedReader reader = request.getReader();
//		while ((line = reader.readLine()) != null) {
//		    jb.append(line);
//		}
        String jsonString = (String) request.getAttribute("object");
        JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(jsonString);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		String jsonData = jb.toString(); 
		String orgName = null;
		String orgType = null;
		String industry = null;
		String contactEmail = null;
		String contactNumber = null;
		String adminName = null;
		String adminEmail = null;
		String adminPassword = null;
//		System.out.println(jsonData);
		try {
//			JSONObject jsonObject = new JSONObject(jsonData);
			orgName = jsonObject.getJSONObject("organization").getString("orgName");
			orgType = jsonObject.getJSONObject("organization").getString("orgType");
			industry = jsonObject.getJSONObject("organization").getString("industry");
			contactEmail = jsonObject.getJSONObject("organization").getString("contactEmail");
			contactNumber = jsonObject.getJSONObject("organization").getString("contactNumber");

			adminName = jsonObject.getJSONObject("admin").getString("adminName");
			adminEmail = jsonObject.getJSONObject("admin").getString("adminEmail");
			adminPassword = jsonObject.getJSONObject("admin").getString("adminPassword");

		
		} 
		
		catch (JSONException e) {
		
			logger.error("json exception in signup servlet while getting data from json object");
			e.printStackTrace();
		
		}
		
		SignUp signUp = new SignUp();
		JSONObject output = null;
		JSONObject responseObject = new JSONObject();
		
		try {
			output = signUp.addOrganisation(orgName, orgType, industry, contactEmail, contactNumber, adminName, adminEmail,adminPassword);
			
			responseObject = output;
		
		}
		
		catch (JSONException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	    	logger.error("Error parsing JSON object. "+e.getMessage());
	    	try {
				responseObject.put("statusCode", 500);
				responseObject.put("message", "Error parsing JSON object.");
			} 
	    	catch (JSONException e1) {
	    		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	    		logger.error("Error parsing JSON object. "+e1.getMessage());
			}
	        
	    } 
		//eg
	    catch (SQLException e) {
	    	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	      e.printStackTrace();
	    	try {
	    		responseObject.put("statusCode", 500);
	    		responseObject.put("message", "Error occurred while retrieving data from the database.");
			} 
	    	catch (JSONException e1) {
	    		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	    		logger.error("Error parsing JSON object. "+e1.getMessage());
			}
	    	logger.error("Error occurred while retrieving data from the database. "+e.getMessage());
	       
	    }
	    
	    
	    response.getWriter().write(responseObject.toString());
		
	}

}
