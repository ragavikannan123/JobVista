package org.servlet.Applicant;

import java.io.BufferedReader;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.recruitment.dao.ApplicantDAO;
import org.recruitment.dao.ApplicantDAOImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ApplyForJobServlet
 */
@WebServlet("/ApplyForJobServlet")
public class ApplyForJobServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	System.out.println("ApplyForJobServlet");

        BufferedReader reader = request.getReader();
        StringBuilder jsonStringBuilder = new StringBuilder();
        String line;
        System.out.println("starting");
        while ((line = reader.readLine()) != null) {
            jsonStringBuilder.append(line);
        }
        System.out.println("while ended");

        String json = jsonStringBuilder.toString();
        System.out.println("jsonStringBuilder");
        try {
            JSONObject jsonObject = new JSONObject(json);
            System.out.println("Extracting values from JSON");
            // Extracting values from JSON
            String name = jsonObject.getString("name");
            String email = jsonObject.getString("email");
            int openingId = jsonObject.getInt("openingId");

            ApplicantDAO applicantDAO = new ApplicantDAOImpl();
            System.out.println("applying for job");
            boolean success = applicantDAO.applyForJob(name, email, openingId);
            System.out.println("applied for job");
            JSONObject responseJson = new JSONObject();
            System.out.println(success);
            if (success) {
                responseJson.put("status", "success");
                responseJson.put("message", "Job application successful.");
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                responseJson.put("status", "error");
                responseJson.put("message", "Failed to apply for the job.");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

            response.getWriter().write(responseJson.toString());
        } catch (JSONException e) {
        	System.out.println("cathcing the error");
            JSONObject responseJson = new JSONObject();
            try {
            	System.out.println("this is where the error is ");
				responseJson.put("status", "error");
				responseJson.put("message", "Error parsing JSON: " + e.getMessage());
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(responseJson.toString());
        }
    }
}
