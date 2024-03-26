package org.recruitment.organization;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.util.CommonLogger;
import org.util.ConnectionClass;
import org.util.Constants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SignUp {
	Logger logger = CommonLogger.getCommon().getLogger(SignUp.class);
	
	public JSONObject addAdmin(String name, String email, String password, int orgId) throws SQLException, JSONException {
		ConnectionClass db = ConnectionClass.CreateCon();
		Connection connection = db.getConnection();
		
		JSONObject response = new JSONObject();
		PreparedStatement preparedStatement = connection.prepareStatement(Constants.addAdmin);
		preparedStatement.setString(1, name);
		preparedStatement.setString(2, email);
		preparedStatement.setInt(3, orgId);
		int affectedRows = preparedStatement.executeUpdate();
		if (affectedRows > 0 ) {
			String sessionId = UUID.randomUUID().toString();
			preparedStatement = connection.prepareStatement(Constants.addUser);
			preparedStatement.setString(1, "Admin");
			preparedStatement.setString(2, password);
			preparedStatement.setString(3, email);
			preparedStatement.setString(4, sessionId);
			int addUser = preparedStatement.executeUpdate();
			
			if(addUser>0) {
				
				preparedStatement = connection.prepareStatement("select Admin_Id from Admin where Email = ?");
				preparedStatement.setString(1, email);
				ResultSet admin = preparedStatement.executeQuery();
				
				int adminId = 0;
				
				while(admin.next()) {
					adminId = admin.getInt("Admin_Id");
				}
				
				logger.info("Admin sucessfully added");
				 response.put("statusCode", 200);
	             response.put("message", "Organization and admin successfully added");
	             JSONObject userDetails = new JSONObject();
	             userDetails.put("role", "Admin");
	             userDetails.put("Org_Id", orgId);
	             userDetails.put("Admin_Id", adminId);
	             response.put("userDetails", userDetails);
	             
	             return response;
			}
		}
		
		logger.info("Failed to add admin");
		response.put("statusCode", 200);
        response.put("message", "Failed to add admin");
        return response;
		
	}
	
	public JSONObject addOrganisation(String orgName, String orgType, String industry, String contactEmail, String contactNumber, String adminName, String adminEmail, String adminPassword) throws SQLException, JSONException {
		
		ConnectionClass db = ConnectionClass.CreateCon();
		Connection connection = db.getConnection();
		
		JSONObject jsonObject = new JSONObject();
		
		PreparedStatement preparedStatement = connection.prepareStatement(Constants.addOrganization);
		preparedStatement.setString(1, orgName);
		preparedStatement.setString(2, orgType);
		preparedStatement.setString(3, industry);
		preparedStatement.setString(4, contactEmail);
		preparedStatement.setString(5, contactNumber);
		int affectedRows = preparedStatement.executeUpdate();
		if (affectedRows <=0 ) {
			logger.info("Failed to add Oraganization");
			jsonObject.put("statusCode", 500);
			jsonObject.put("message", "Failed to add Oraganization");
			return jsonObject;
		}
		
		preparedStatement = connection.prepareStatement(Constants.getOrgId);
		preparedStatement.setString(1, orgName);
		ResultSet resultSet = preparedStatement.executeQuery();
		int org_id = 0;
		while(resultSet.next()) {
			logger.info("Organization successfully added");
			org_id = resultSet.getInt("Org_Id");
		}
		return addAdmin(adminName, adminEmail, adminPassword, org_id);
		
	}
	
}
