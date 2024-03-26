package org.servlet.panelist.opening;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
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

@WebServlet("/PanelistOpeningServlet")
public class PanelistOpeningServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doOptions(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		response.setStatus(HttpServletResponse.SC_OK);
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");

		Logger log = CommonLogger.getCommon().getLogger(PanelistOpeningServlet.class);
		
		JSONObject responseData = new JSONObject();
		AdminManagement adminManagement = new AdminManagement();
		OpeningDetailsDAO OpeningDetailsDAO = new OpeningDetailsDAO();
		StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
		int orgId = 0;
		int panelistId = 0;
		try {
			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONObject userDetails = jsonObject.getJSONObject("userDetails");
			orgId = userDetails.getInt("Org_Id");
			panelistId = userDetails.getInt("Panelist_Id");
		} 
		
		catch (JSONException e) {
		    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		    e.printStackTrace();
		
		}
		
		List<JSONObject> openingDetailsList = new ArrayList<>();
		openingDetailsList = OpeningDetailsDAO.getOpeningsForPanelist(panelistId,openingDetailsList);
		

		
		List<JSONObject> currentOpeningDetailsList = new ArrayList<>();
		currentOpeningDetailsList = OpeningDetailsDAO.getCurrentOpeningsForPanelist(panelistId,currentOpeningDetailsList);
		

		
		List<JSONObject> upComingOpeningDetailsList = new ArrayList<>();
		upComingOpeningDetailsList = OpeningDetailsDAO.getUpComingOpeningsForPanelist(panelistId,upComingOpeningDetailsList);
		

		
		JSONObject jsonResponse = new JSONObject();
		JSONObject jsonResponseForPanelist = new JSONObject();

		try {
		    jsonResponseForPanelist.put("openings", openingDetailsList);
		    jsonResponseForPanelist.put("currentOpenings", currentOpeningDetailsList);
		    jsonResponseForPanelist.put("upComingOpenings", upComingOpeningDetailsList);
		    
		    
		    log.info(jsonResponseForPanelist.toString());
		    jsonResponse.put("Status", "Success");
		    jsonResponse.put("Value", jsonResponseForPanelist);
		    response.setStatus(HttpServletResponse.SC_OK);

		} catch (JSONException e) {
		    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		    e.printStackTrace();
		}

		response.setContentType("application/json");
		System.out.println(jsonResponse.toString());
		System.out.println("This is");
		// Write JSON response
		response.getWriter().print(jsonResponse.toString());


//		JSONObject jsonResponse = new JSONObject();
//		JSONObject jsonResponseforPanelist = new JSONObject();
//		try {
//			jsonResponseforPanelist.put("openings", openingDetailsList);
//			jsonResponseforPanelist.put("currentOpenings", currentOpeningDetailsList);
//			jsonResponse.put("Status", "Success");
//			jsonResponse.put("Value", jsonResponseforPanelist);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			e.printStackTrace();
//		}
//
//		response.setContentType("application/json");
//
//		// Write JSON response
//		response.getWriter().print(jsonResponse.toString());

	}
}