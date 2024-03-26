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
 * Servlet implementation class RemoveApplicantServlet
 */
@WebServlet("/RemoveApplicantServlet")
public class RemoveApplicantServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        BufferedReader reader = request.getReader();
        StringBuilder jsonStringBuilder = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            jsonStringBuilder.append(line);
        }

        String json = jsonStringBuilder.toString();

        try {
            JSONObject jsonObject = new JSONObject(json);
            
            // Extracting values from JSON
            String name = jsonObject.getString("name");
            String email = jsonObject.getString("email");

            // Assuming you have an ApplicantDAO instance
            ApplicantDAO applicantDAO = new ApplicantDAOImpl();
            boolean success = applicantDAO.removeApplicant(name, email);

            JSONObject responseJson = new JSONObject();

            if (success) {
                responseJson.put("status", "success");
                responseJson.put("message", "Applicant removed successfully.");
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                responseJson.put("status", "error");
                responseJson.put("message", "Failed to remove applicant.");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

            response.getWriter().write(responseJson.toString());
        } catch (JSONException e) {
            JSONObject responseJson = new JSONObject();
            try {
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
