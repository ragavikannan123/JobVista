package org.servlet.panelist;

import org.json.JSONException;
import org.json.JSONObject;
import org.recruitment.users.InterviewerService;
import org.util.ConnectionClass;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/InterviewerAddReviewServlet")
public class InterviewerAddReviewServlet extends HttpServlet {

    private final InterviewerService interviewerService = new InterviewerService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject jsonResponse = new JSONObject();
        resp.setContentType("application/json");

        int panelistId = Integer.parseInt(req.getParameter("panelistId"));
        int jobSeekerId = Integer.parseInt(req.getParameter("jobSeekerId"));
        String review = req.getParameter("review");
        int points = Integer.parseInt(req.getParameter("points"));

        try (Connection connection = ConnectionClass.CreateCon().getConnection()) {
            interviewerService.addReview(panelistId, jobSeekerId, review, points);
            jsonResponse.put("status", "success");
            jsonResponse.put("message", "Review added successfully.");
        } catch (SQLException e) {
            try {
				jsonResponse.put("status", "error");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            try {
				jsonResponse.put("message", "Error adding review: " + e.getMessage());
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        resp.getWriter().write(jsonResponse.toString());
    }
}
