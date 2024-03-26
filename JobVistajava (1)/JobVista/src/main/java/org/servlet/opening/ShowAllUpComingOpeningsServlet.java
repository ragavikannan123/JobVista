package org.servlet.opening;

import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.recruitment.dao.OpeningDetailsDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ShowAllUpComingOpeningsServlet")
public class ShowAllUpComingOpeningsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            OpeningDetailsDAO openingDetailsDAO = new OpeningDetailsDAO();
            List<JSONObject> openingDetailsList = openingDetailsDAO.getAllUpComingOpenings();

            // Create a JSON object for the response
            JSONObject jsonResponse = new JSONObject();

            // Set response status to success
            jsonResponse.put("Status", "Success");
            jsonResponse.put("Value", new JSONArray(openingDetailsList));

            // Set response content type
            response.setContentType("application/json");

            // Write JSON response
            response.getWriter().print(jsonResponse.toString());
        } catch (Exception e) {
            // Handle exceptions and send an error JSON response
            JSONObject errorResponse = new JSONObject();
            
            try {
            	errorResponse.put("Status", "Error");
				errorResponse.put("Error", e.getMessage());
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

            // Set response status code to indicate an error
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            // Set response content type
            response.setContentType("application/json");

            // Write JSON error response
            response.getWriter().print(errorResponse.toString());
        }
    }
}
