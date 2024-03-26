package org.servlet.panelist;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONException;
import org.json.JSONObject;
import org.recruitment.tests.Test;
import org.recruitment.tests.TestManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AddTestToOpening
 */
@WebServlet("/AddTestToOpening")
public class AddTestToOpening extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public AddTestToOpening() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		doGet(request, response);
		
		response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        
        
		
		Logger logger = Logger.getLogger(CreateOpening.class);
		PropertyConfigurator.configure("/home/abi-zstk360/log4.properties");
		JSONObject responseJson = new JSONObject();
		StringBuilder jsonLoad = new StringBuilder();
        try {
            logger.info("Starting to read JSON payload from request.");

            BufferedReader br = request.getReader();
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                jsonLoad.append(line);
            }
            
            System.out.println("js: " + jsonLoad.toString());
            logger.info("JSON payload successfully parsed.");
            JSONObject testJson = new JSONObject(jsonLoad.toString());
            System.out.println("test: " + testJson.toString());
            
            String dobString = testJson.getString("date");
    		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    		Date date = null;
            
			Test newTest = new Test(testJson.getString("title"), dateFormat.parse(dobString), testJson.getInt("duration"), testJson.getString("type"));
			int openingId = testJson.getInt("openingId");
			TestManager.getTestManager().addTest(openingId, newTest);
			
			responseJson.put("status", 200);
			responseJson.put("message", "Test added");
			
			
			response.getWriter().write(responseJson.toString());
			
			
            
            
            
            
        }
        catch(Exception e) {
        	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        	e.printStackTrace();
        	try {
	        	responseJson.put("status", 500);
	        	responseJson.put("message", e.getMessage());
	        	response.getWriter().write(responseJson.toString());
        	}
        	catch (JSONException e1) {
        		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        		logger.error("json exception in signup servlet while getting data from json object");
    			e.printStackTrace();
        	}
        }
		
		
        System.out.println("res: " + responseJson.toString());
		
	}

}
