package org.servlet.admin.opening;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.recruitment.dao.OpeningDetailsDAO;
import org.recruitment.organization.AdminManagement;
import org.util.CommonLogger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class getOpeningsWithDepartment
 */
@WebServlet("/GetOpenings")
public class GetOpenings extends HttpServlet {

	Logger logger = CommonLogger.getCommon().getLogger(GetOpenings.class);
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetOpenings() {
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");

		OpeningDetailsDAO OpeningDetailsDAO = new OpeningDetailsDAO();
		
		StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
		
		int orgId = 0;
		int adminId = 0;
		System.out.println(sb + "a");
		try {
			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONObject userDetails = jsonObject.getJSONObject("userDetails");
			orgId = userDetails.getInt("Org_Id");
			adminId = userDetails.getInt("Admin_Id");
		} 
		
		catch (JSONException e) {
		e.printStackTrace();
			logger.error("json exception while getting data from json object \n"+e.getMessage());
		
		}
		

		List<JSONObject> openingDetailsList = OpeningDetailsDAO.getAllOpeningsForAdmin(orgId);
		JSONObject jsonResponse = new JSONObject();
		
        try {
        	System.out.println(openingDetailsList);
			jsonResponse.put("Status", "Success");
			jsonResponse.put("Value", new JSONArray(openingDetailsList));
		}
      
        catch (JSONException e) {
        	e.printStackTrace();
			logger.error("Admin:"+adminId+"\nError parsing JSON object.\n" + e.getMessage());
	    
			try {
				jsonResponse.put("statusCode", 500);
				jsonResponse.put("message", "Error parsing JSON object.\n");
			} 
	    	catch (JSONException e1) {
	    		e.printStackTrace();
	    		logger.error("Admin:"+adminId+"\nError parsing JSON object." + e1.getMessage());
			}
		
        }
        
        response.setContentType("application/json");

        // Write JSON response
        response.getWriter().print(jsonResponse.toString());

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

}
