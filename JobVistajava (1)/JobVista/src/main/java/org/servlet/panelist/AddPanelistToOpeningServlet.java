package org.servlet.panelist;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.recruitment.users.InterviewerService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class AddPanelistToOpeningServlet
 */
@WebServlet("/AddPanelistToOpeningServlet")
public class AddPanelistToOpeningServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Read JSON data from the request body using BufferedReader
        BufferedReader reader = request.getReader();
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }

        // Parse the JSON data using JSONObject
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonBuilder.toString());

            // Extract Opening ID
            int openingId = jsonObject.getInt("openingId");

            // Extract List of Panelist Emails
            List<String> panelistEmails = new ArrayList<>();
            JSONArray emailsArray = jsonObject.getJSONArray("emails");
            for (int i = 0; i < emailsArray.length(); i++) {
                panelistEmails.add(emailsArray.getString(i));
            }

            // Call the InterviewerService method to add Panelist to Opening for each email
            InterviewerService interviewerService = new InterviewerService();
            for (String email : panelistEmails) {
            	System.out.println(email);
                boolean success = interviewerService.addPanelistToOpening(openingId, email);

                if (success) {
                    response.getWriter().write("Panelist added to opening successfully");
                } else {
                    response.getWriter().write("Failed to add Panelist to opening");
                    // Handle failure scenario (e.g., log, return an error response, etc.)
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle JSON parsing exception
            response.getWriter().write("Failed to parse JSON");
        }
    }

}
