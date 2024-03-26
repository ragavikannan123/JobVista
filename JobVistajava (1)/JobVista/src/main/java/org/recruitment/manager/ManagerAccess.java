package org.recruitment.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import org.util.CommonLogger;
import org.util.ConnectionClass;

public class ManagerAccess {
	Logger logger = CommonLogger.getCommon().getLogger(ManagerAccess.class);
	public String addTemplate(int openingId, String typeOfTest) throws SQLException {
		ConnectionClass db = ConnectionClass.CreateCon();
		Connection connection = db.getConnection();
		
		String query = "select * from Template where Opening_Id = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		preparedStatement.setInt(1, openingId);
		ResultSet resultSet = preparedStatement.executeQuery();
		
		int round = 0;
		while (resultSet.next()) {
			round++;
		}
		
		query = "insert into Template (Opening_Id, TypeOfTest, RoundOn) values (?, ?, ?)";
		preparedStatement = connection.prepareStatement(query);
		preparedStatement.setInt(1, openingId);
		preparedStatement.setString(1, typeOfTest);
		preparedStatement.setInt(3, round);
		int affectedrows = preparedStatement.executeUpdate();
		if(affectedrows > 0) {
			logger.info("Template successfully added");
			return "Template added successfully";
		}
		logger.info("Failed to add template");
		return "Failed to add template";
	}
}
