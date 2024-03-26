package org.servlet.admin.graph;

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
 * Servlet implementation class Graphs
 */
@WebServlet("/Graphs")
public class Graphs extends HttpServlet {
	
	Logger logger = CommonLogger.getCommon().getLogger(Graphs.class);
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Graphs() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
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
		
		StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
		
		int orgId = 0;
		int adminId = 1;
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
	        
	        JSONObject openingGraphWithDepartments = adminManagement.getOpeningGraphWithDepartments(orgId);
	        JSONObject openingsGraphByMonth = adminManagement.getOpeningsGraphByMonth(orgId);
	        JSONObject applicantsStatusGraph = adminManagement.getApplicantsStatusGraph(orgId);
	        JSONObject selectedApplicantsGraphInDepartments = adminManagement.selectedApplicantsGraphInDepartments(orgId);
	        JSONObject selectedApplicantsGraphInMonth = adminManagement.selectedApplicantsGraphInMonth(orgId);
	        JSONObject differenceInYearByOpenings = adminManagement.differenceInYearByOpenings(orgId);
	        JSONObject differenceInMonthByHired = adminManagement.differenceInMonthByHired(orgId);
	        
	        responseData.put("statusCode", 200);
	        responseData.put("openingGraphWithDepartments", openingGraphWithDepartments);
	        responseData.put("openingsGraphByMonth", openingsGraphByMonth);
	        responseData.put("applicantsStatusGraph", applicantsStatusGraph);
	        responseData.put("selectedApplicantsGraphInDepartments", selectedApplicantsGraphInDepartments);
	        responseData.put("selectedApplicantsGraphInMonth", selectedApplicantsGraphInMonth);
	        responseData.put("differenceInYearByOpenings", differenceInYearByOpenings);
	        responseData.put("differenceInMonthByHired", differenceInMonthByHired);
	        
	        
	    } 
	    catch (JSONException e) {
	        
	    	logger.error("Admin:"+adminId+"\nError parsing JSON object.\n" + e.getMessage());
	    	try {
				responseData.put("statusCode", 500);
				responseData.put("message", "Error parsing JSON object.");
			} 
	    	catch (JSONException e1) {
	    		logger.error("Admin:"+adminId+"\nError parsing JSON object." + e1.getMessage());
			}
	        e.printStackTrace();
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
	        e.printStackTrace();
	    }
	    response.getWriter().write(responseData.toString());
	
	}

}
