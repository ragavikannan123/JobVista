package org.recruitment.users;

import org.util.ConnectionClass;
import org.util.Constants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InterviewerService {
	
    public void addReview(int panelistId, int jobSeekerId, String review, int points) throws SQLException {
        try (Connection connection = ConnectionClass.CreateCon().getConnection()) {
            String query = "INSERT INTO NotesFromPanelist (Panelist_Id, Job_Seeker_Id, Review, Points) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, panelistId);
                preparedStatement.setInt(2, jobSeekerId);
                preparedStatement.setString(3, review);
                preparedStatement.setInt(4, points);
                preparedStatement.executeUpdate();
            }
        }
    }

    public void setMarks(int jobSeekerId, int testId, int marks, String status) throws SQLException {
        try (Connection connection = ConnectionClass.CreateCon().getConnection()) {
            String query = "INSERT INTO Result (Job_Seeker_Id, Test_Id, Status, Points) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, jobSeekerId);
                preparedStatement.setInt(2, testId);
                preparedStatement.setString(3, status);
                preparedStatement.setInt(4, marks);
                
                preparedStatement.executeUpdate();
            }
        }
    }
	public boolean addPanelistToOpening(int openingId, String email) {
		Connection conn = ConnectionClass.CreateCon().getConnection();
		PreparedStatement assinginterviwers;
		try {
			assinginterviwers = conn.prepareStatement(Constants.assignPanelistToOpenings);
			assinginterviwers.setString(1, email);
			assinginterviwers.setInt(2,openingId);
			
			assinginterviwers.executeUpdate();
			System.out.println("Working\n" +assinginterviwers);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

}
