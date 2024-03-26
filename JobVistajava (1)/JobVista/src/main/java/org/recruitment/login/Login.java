package org.recruitment.login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import org.util.ConnectionClass;
import org.util.Constants;

import jakarta.servlet.http.Cookie;

public class Login {
	
	String role = null;
	public String checkUser(String email, String password) throws SQLException {
		
		ConnectionClass db = ConnectionClass.CreateCon();
		Connection connection = db.getConnection();
		
        PreparedStatement statement = connection.prepareStatement(Constants.checkUser);
        statement.setString(1, email);
        statement.setString(2, password);
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
        	this.role = rs.getString("Role");
        	System.err.println("Logged in successfully");
            return "Logged in successfully";
        }
        System.err.println("Invalid email or password");
        return "Invalid email or password";
	}
	
	public JSONObject updateSession(String email) throws SQLException, JSONException {
	    String sessionId = UUID.randomUUID().toString();
	    ConnectionClass db = ConnectionClass.CreateCon();
	    Connection connection = db.getConnection();

	    PreparedStatement updateSession = connection.prepareStatement(Constants.updateSession);
	    updateSession.setString(1, sessionId);
	    updateSession.setString(2, email);
	    updateSession.executeUpdate();

	    Cookie sessionIdCookie = new Cookie("session_Id", sessionId);
	    sessionIdCookie.setMaxAge(86400);

	    JSONObject jsonObject = new JSONObject();

	    

	    int panelistId = 0;
	    int adminId = 0;

	    if (this.role.equalsIgnoreCase("Admin")) {
	        PreparedStatement getAdminId = connection.prepareStatement(Constants.adminId);
	        getAdminId.setString(1, email);
	        ResultSet adminResultSet = getAdminId.executeQuery();
	        while (adminResultSet.next()) {
	            adminId = adminResultSet.getInt("Admin_Id");
	        }
	    } else {
	        PreparedStatement getPanelistId = connection.prepareStatement(Constants.panelistId);
	        getPanelistId.setString(1, email);
	        ResultSet panelistResultSet = getPanelistId.executeQuery();
	        while (panelistResultSet.next()) {
	            panelistId = panelistResultSet.getInt("Panelist_Id");
	        }
	    }

	    if (panelistId != 0) {
	    	jsonObject.put("Panelist_Id", panelistId);
	    	jsonObject.put("role", "panelist");
	    	PreparedStatement getOrgId = connection.prepareStatement(Constants.OrgIdForPanelist);
		    getOrgId.setString(1, email);
		    ResultSet orgResultSet = getOrgId.executeQuery();
		    while (orgResultSet.next()) {
		    	jsonObject.put("Org_Id", orgResultSet.getInt("Org_Id"));
		    }
	    }

	    if (adminId != 0) {
	    	jsonObject.put("Admin_Id", adminId);
	    	jsonObject.put("role", "admin");
	    	PreparedStatement getOrgId = connection.prepareStatement(Constants.OrgId);
		    getOrgId.setString(1, email);
		    ResultSet orgResultSet = getOrgId.executeQuery();
		    while (orgResultSet.next()) {
		    	jsonObject.put("Org_Id", orgResultSet.getInt("Org_Id"));
		    }
	    }
	    System.out.println("session created");
	    return jsonObject;
	
	}

	
}
