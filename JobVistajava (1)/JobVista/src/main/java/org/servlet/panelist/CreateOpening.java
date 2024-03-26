package org.servlet.panelist;

import java.awt.Panel;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.recruitment.jobs.Openings;
import org.recruitment.tests.Test;
import org.recruitment.tests.TestManager;
import org.recruitment.users.Panelist;
import org.recruitment.users.PanelistManager;
import org.servlet.admin.opening.GetCurrentOpenings;
import org.util.CommonLogger;
import org.util.Gender;

import com.mysql.cj.xdevapi.JsonArray;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CreateOpening
 */
@WebServlet("/CreateOpening")
public class CreateOpening extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public CreateOpening() {
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
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub	
		response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        
		Logger logger = CommonLogger.getCommon().getLogger(CreateOpening.class);

		JSONObject responseJson = new JSONObject();
		StringBuilder jsonLoad = new StringBuilder();
		
		int panelistId = 0;
        
        try {
            logger.info("Starting to read JSON payload from request.");

            BufferedReader br = request.getReader();
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                jsonLoad.append(line);
            }
        
//            JSONObject jsonData = new JSONObject(request.getParameter("JSON"));
            JSONObject jsonData = new JSONObject(jsonLoad.toString());
            logger.info("JSON payload successfully parsed.");
            
            JSONObject userDetails = jsonData.getJSONObject("userDetails");
			panelistId = userDetails.getInt("Panelist_Id");

//            JSONObject panelistJson = jsonData.getJSONObject("panelist");
            JSONObject openingJson = jsonData.getJSONObject("opening");
            JSONObject testJson = jsonData.getJSONObject("test");
//
//            Panelist panelist = new Panelist(
//                panelistJson.getString("name"),
//                panelistJson.getString("email"),
//                Gender.valueOf(panelistJson.getString("gender").toUpperCase()),
////                Gender.valueOf("FEMALE"),
//                panelistJson.getString("position"),
//                panelistJson.getString("organistion"), // Fixed typo: organistion -> organization
//                panelistJson.getString("department")
//            );
            
            
            List <Panelist> interviwers = new ArrayList<Panelist>();
            JSONArray interviwersJsonArr = openingJson.getJSONArray("interviewers");
//            System.out.println(interviwersJsonArr.length());
            
            for (int i = 0; i < interviwersJsonArr.length(); i++) {
            	JSONObject interviwerJson = interviwersJsonArr.getJSONObject(i);
            	interviwers.add(new Panelist(null,interviwerJson.getString("email")));
			}
            
//            System.out.println(interviwers.size());
            
            
            Openings opening = new Openings(
                openingJson.getString("title"),
                openingJson.getInt("experience"),
                openingJson.getString("qualification"),
                openingJson.getString("department"),
                openingJson.getString("type"),
                openingJson.getString("salaryRange"), null
            );

            logger.info("Creating panelist and opening objects.");

            int OpeningId = PanelistManager.getPanelistManger().addOpening(panelistId, opening , interviwers);
            
            
            
//            testJson
            
            LocalDate date = LocalDate.parse(testJson.getString("date"));
            
            Date date2 = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
            
//            Date today = new Date(date);
            
            System.out.println("Before test");
            
			Test newTest = new Test(testJson.getString("title"), date2, testJson.getInt("duration"), testJson.getString("typeOfTest"));
            System.out.println("justBefore test");
			TestManager.getTestManager().addTest(OpeningId, newTest);
            

            logger.info("Opening added successfully.");
            
            System.out.println("After test");
            responseJson.put("status",200);
            responseJson.put("openingId",OpeningId);
            
            
            response.getWriter().write(responseJson.toString());

        } catch (Exception e) {
            logger.error(e.getMessage());
            try {
            	responseJson.put("status",500);
                responseJson.put("error",e.getMessage());
            	
                response.getWriter().write(responseJson.toString());
            } catch (Exception ioException) {
                logger.error(e.getMessage());
            }
        }

		
		
		
	}

}
