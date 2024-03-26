package org.recruitment.organization;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.util.CommonLogger;
import org.util.ConnectionClass;
import org.util.Constants;
import org.util.Gender;

public class AdminManagement {
	
	Logger logger = CommonLogger.getCommon().getLogger(AdminManagement.class);

	
	//This method will add the panelist
	public String addPanelist(String name, String email, Gender gender, String position, String password, String department, int orgId) throws SQLException {
		
		ConnectionClass db = ConnectionClass.CreateCon();
		Connection connection = db.getConnection();
		
		PreparedStatement getDepartment= connection.prepareStatement(Constants.getDepartment);
		getDepartment.setString(1, department);
		ResultSet departments = getDepartment.executeQuery();
		int departmentId = 0;
		while(departments.next()) {
			departmentId = departments.getInt("Department_Id");
		}
 		
		PreparedStatement checkPreparedStatement = connection.prepareStatement(Constants.isPanelistExists);
		checkPreparedStatement.setString(1, email);
		checkPreparedStatement.setInt(2, orgId);
		ResultSet panelists = checkPreparedStatement.executeQuery();
		if (panelists.next()) {
			return "Panelist already exists";
		}
		
		
		
		PreparedStatement preparedStatement = connection.prepareStatement(Constants.addPanelist);
		preparedStatement.setString(1, name);
		preparedStatement.setString(2, email);
		preparedStatement.setString(3, gender.getValue());
		preparedStatement.setInt(4, departmentId);
		preparedStatement.setInt(5, orgId);
		preparedStatement.setString(6, position);
        int affectedRows = preparedStatement.executeUpdate();
		
		if(affectedRows > 0) {
			String sessionId = UUID.randomUUID().toString();
			preparedStatement = connection.prepareStatement(Constants.addUser);
			preparedStatement.setString(1, "Panelist");
			preparedStatement.setString(2, password);
			preparedStatement.setString(3, email);
			preparedStatement.setString(4, sessionId);
			int addUser = preparedStatement.executeUpdate();
			
			if(addUser>0) {
				logger.info("Panelist added");
				return "Panelist added";
			}
		}
		logger.info("Failed to add panelist");
		return "Failed to add panelist";
		
	}
	
	public boolean removePanelist(int panelistId) throws SQLException {
		
		ConnectionClass db = ConnectionClass.CreateCon();
		Connection connection = db.getConnection();
		
		PreparedStatement preparedStatement = connection.prepareStatement(Constants.removePanelist);
		preparedStatement.setInt(1, panelistId);
		int affectedRows = preparedStatement.executeUpdate();
		logger.info("Panelist removed");
		return affectedRows > 0;
		
	}
	
	public JSONArray getPanelists(int orgId) throws SQLException, JSONException {

	    
		ConnectionClass db = ConnectionClass.CreateCon();
	    Connection connection = db.getConnection();
	    
	    PreparedStatement preparedStatement = connection.prepareStatement(Constants.getPanelist);
	    preparedStatement.setInt(1, orgId);
	    ResultSet resultSet = preparedStatement.executeQuery();
	    
	    JSONArray jsonResult = new JSONArray();
	    
	    while (resultSet.next()) {
	        
	    	int id = resultSet.getInt("Panelist_Id");
	        JSONObject details = new JSONObject();
	        details.put("id",id);
	        details.put("Name",resultSet.getString("Name"));
	        details.put("Email",resultSet.getString("Email"));
	        details.put("Gender",resultSet.getString("Gender"));
	        details.put("Position",resultSet.getString("Position"));
	        details.put("Title",resultSet.getString("Title"));
	      
			jsonResult.put(details);
	    }
	    
	    return jsonResult;
	}
	
	public JSONArray getPanelistsWithDepartment(int orgId, int departmentId) throws SQLException, JSONException {
	   
		ConnectionClass db = ConnectionClass.CreateCon();
	    Connection connection = db.getConnection();
	    
	    PreparedStatement preparedStatement = connection.prepareStatement(Constants.getPanelistWithDepartment);
	    preparedStatement.setInt(1, orgId);
	    preparedStatement.setInt(2, departmentId);
	    ResultSet resultSet = preparedStatement.executeQuery();
	    
	    JSONArray jsonResult = new JSONArray();
	    
	    while (resultSet.next()) {
	        
	    	int id = resultSet.getInt("Panelist_Id");
	        JSONObject details = new JSONObject();
	        details.put("id",id);
	        details.put("Name",resultSet.getString("Name"));
	        details.put("Email",resultSet.getString("Email"));
	        details.put("Gender",resultSet.getString("Gender"));
	        details.put("Position",resultSet.getString("Position"));
	        details.put("Title",resultSet.getString("Title"));
	      
			jsonResult.put(details);
	    }
	    
	    return jsonResult;
	}
	
	public JSONArray getPanelistWithName(int panelistId) throws SQLException, JSONException {
	   
		ConnectionClass db = ConnectionClass.CreateCon();
	    Connection connection = db.getConnection();
	   
	    PreparedStatement preparedStatement = connection.prepareStatement(Constants.getPanelistWithName);
	    preparedStatement.setInt(1, panelistId);
	    ResultSet resultSet = preparedStatement.executeQuery();
	    
	    JSONArray jsonResult = new JSONArray();
	    
	    while (resultSet.next()) {
	        
	    	int id = resultSet.getInt("Panelist_Id");
	        JSONObject details = new JSONObject();
	        details.put("id",id);
	        details.put("Name",resultSet.getString("Name"));
	        details.put("Email",resultSet.getString("Email"));
	        details.put("Gender",resultSet.getString("Gender"));
	        details.put("Position",resultSet.getString("Position"));
	        details.put("Title",resultSet.getString("Title"));
	      
			jsonResult.put(details);
	    }
	    
	    return jsonResult;
	}
	
	public boolean editPanelist(int panelistId, JSONObject panelistDetails) throws JSONException, SQLException {
		ConnectionClass db = ConnectionClass.CreateCon();
		Connection connection = db.getConnection();	
		
		String name = panelistDetails.getString("name");
		String email = panelistDetails.getString("email");
		String position = panelistDetails.getString("position");
				
		PreparedStatement preparedStatement = connection.prepareStatement(Constants.editPanelist);
		preparedStatement.setString(1, name);
		preparedStatement.setString(2, email);
		preparedStatement.setString(3, position);
		preparedStatement.setInt(4, panelistId);
		int result = preparedStatement.executeUpdate();
		logger.info("Panelist details was updated");
		return result > 0;
	}

	public String addDepartment(String title, String description, int orgId) throws SQLException {
		
		
		ConnectionClass db = ConnectionClass.CreateCon();
		Connection connection = db.getConnection();
		
		PreparedStatement checkPreparedStatement = connection.prepareStatement(Constants.isDepartmentExists);
		checkPreparedStatement.setString(1, title);
		checkPreparedStatement.setInt(2, orgId);
		ResultSet departments = checkPreparedStatement.executeQuery();
		if (departments.next()) {
			return "Department already exists";
		}
		
		PreparedStatement preparedStatement = connection.prepareStatement(Constants.addDepartment);
		preparedStatement.setString(1, title);
		preparedStatement.setInt(2, orgId);
		preparedStatement.setString(3, description);
        int affectedRows = preparedStatement.executeUpdate();
		
		if(affectedRows > 0) {
			logger.info("Department added");
		
			return "Department added";
		}
		logger.info("Failed to add department");
		return "Failed to add department";
		
	}
	
	public boolean removeDepartments(int departmentId) throws SQLException {
		
		ConnectionClass db = ConnectionClass.CreateCon();
		Connection connection = db.getConnection();

		PreparedStatement preparedStatement = connection.prepareStatement(Constants.removeDepartment);
		preparedStatement.setInt(1, departmentId);
		int affectedRows = preparedStatement.executeUpdate();
		logger.info("Department removed");
		return affectedRows > 0;
		
	}
	
	public JSONArray getDepartments(int orgId) throws SQLException, JSONException {
	    ConnectionClass db = ConnectionClass.CreateCon();
	    Connection connection = db.getConnection();
	    
	    PreparedStatement preparedStatement = connection.prepareStatement(Constants.getDepartments);
	    preparedStatement.setInt(1, orgId);
	    ResultSet resultSet = preparedStatement.executeQuery();
	    
	    JSONArray jsonResult = new JSONArray();
	    
	    while (resultSet.next()) {
	        
	    	int id = resultSet.getInt("Department_Id");
	    	JSONObject details = new JSONObject();
	        details.put("id",id);
	        details.put("Title",resultSet.getString("Title"));
	        details.put("Description",resultSet.getString("Description"));
	       
			jsonResult.put(details);
	    }
	    
	    return jsonResult;
	}
	
	public JSONArray getOpenings(int orgId) throws SQLException, JSONException {
		ConnectionClass db = ConnectionClass.CreateCon();
	    Connection connection = db.getConnection();
	    
	    PreparedStatement preparedStatement = connection.prepareStatement(Constants.getOpenings);
	    preparedStatement.setInt(1, orgId);
	    ResultSet resultSet = preparedStatement.executeQuery();
	    
	    JSONArray jsonResult = new JSONArray();
	    
	    while (resultSet.next()) {
	        
	    	int id = resultSet.getInt("Opening_Id");
	    	JSONObject details = new JSONObject();
	        details.put("id",id);
	        details.put("Panelist_Id",resultSet.getInt("Panelist.Panelist_Id"));
	        details.put("Description",resultSet.getString("Description"));
	        details.put("Experience",resultSet.getInt("Experience"));
	        details.put("Qualification",resultSet.getString("Qualification"));
	        details.put("Departments",resultSet.getString("Departments"));
	        details.put("EmploymentType",resultSet.getString("EmploymentType"));
	        details.put("SalaryRange",resultSet.getString("SalaryRange"));
	        details.put("Name",resultSet.getString("Panelist.Name"));
	        details.put("Start_Date",resultSet.getString("Start_Date"));
	        details.put("End_Date",resultSet.getString("End_Date"));
	        
			jsonResult.put(details);
	    }
	    
	    return jsonResult;
	}
	
	public JSONArray getOpeningsWithDepartment(int departmentId) throws SQLException, JSONException {
		ConnectionClass db = ConnectionClass.CreateCon();
	    Connection connection = db.getConnection();
	    
	    PreparedStatement preparedStatement = connection.prepareStatement(Constants.getOpeningsWithDepartment);
	    preparedStatement.setInt(1, departmentId);
	    ResultSet resultSet = preparedStatement.executeQuery();
	    
	    JSONArray jsonResult = new JSONArray();
	    
	    while (resultSet.next()) {
	        
	    	int id = resultSet.getInt("Opening_Id");
	    	JSONObject details = new JSONObject();
	        details.put("id",id);
	        details.put("Panelist_Id",resultSet.getInt("Panelist_Id"));
	        details.put("Description",resultSet.getString("Description"));
	        details.put("Experience",resultSet.getInt("Experience"));
	        details.put("Qualification",resultSet.getString("Qualification"));
	        details.put("Departments",resultSet.getString("Departments"));
	        details.put("EmploymentType",resultSet.getString("EmploymentType"));
	        details.put("SalaryRange",resultSet.getString("SalaryRange"));
	        details.put("Name",resultSet.getString("Name"));
	        details.put("Start_Date",resultSet.getString("Start_Date"));
	        details.put("End_Date",resultSet.getString("End_Date"));
	        
			jsonResult.put(details);
	    }
	    
	    return jsonResult;
	}
	
	public JSONArray getCurrentOpenings(int orgId) throws SQLException, JSONException {
		ConnectionClass db = ConnectionClass.CreateCon();
	    Connection connection = db.getConnection();
	    LocalDate today = LocalDate.now();
		Date sqlDate = Date.valueOf(today);
	    
	    PreparedStatement preparedStatement = connection.prepareStatement(Constants.getCurrentOpenings);
	    preparedStatement.setInt(1, orgId);
	    preparedStatement.setDate(2, sqlDate);
	    ResultSet resultSet = preparedStatement.executeQuery();
	    
	    JSONArray jsonResult = new JSONArray();
	    
	    while (resultSet.next()) {
	        
	    	int id = resultSet.getInt("Opening_Id");
	    	JSONObject details = new JSONObject();
	        details.put("id",id);
	        details.put("Panelist_Id",resultSet.getInt("Panelist_Id"));
	        details.put("Description",resultSet.getString("Description"));
	        details.put("Experience",resultSet.getInt("Experience"));
	        details.put("Qualification",resultSet.getString("Qualification"));
	        details.put("Departments",resultSet.getString("Departments"));
	        details.put("EmploymentType",resultSet.getString("EmploymentType"));
	        details.put("SalaryRange",resultSet.getString("SalaryRange"));
	        details.put("Name",resultSet.getString("Name"));
	        details.put("Start_Date",resultSet.getString("Start_Date"));
	        details.put("End_Date",resultSet.getString("End_Date"));
	        
			jsonResult.put(details);
	    }
	    
	    return jsonResult;
	}
	
	public JSONObject getOpeningGraphWithDepartments(int org_id) throws SQLException, JSONException {
	    ConnectionClass db = ConnectionClass.CreateCon();
	    Connection connection = db.getConnection();
	    JSONObject json = new JSONObject();
	    
	    PreparedStatement preparedStatement = connection.prepareStatement(Constants.getOpeningGraphWithDepartments);
	    preparedStatement.setInt(1, org_id);
	    ResultSet resultSet = preparedStatement.executeQuery();
	    
	    while(resultSet.next()) {
	        String departmentTitle = resultSet.getString("DepartmentTitle");
	        int count = resultSet.getInt("Count");
	        if (count == 0) {
	            json.put(departmentTitle, 0);
	        } else {
	            json.put(departmentTitle, count);
	        }
	    }
	    
	    return json;
	}

	
	public JSONObject getOpeningsGraphByMonth(int org_id) throws SQLException, JSONException {
	    ConnectionClass db = ConnectionClass.CreateCon();
	    Connection connection = db.getConnection();
	    JSONObject json = new JSONObject();

	    PreparedStatement preparedStatement = connection.prepareStatement(Constants.getOpeningGraphByMonth);
	    preparedStatement.setInt(1, org_id);
	    ResultSet resultSet = preparedStatement.executeQuery();

	    String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	    for (String monthName : monthNames) {
	        json.put(monthName, 0);
	    }

	    while (resultSet.next()) {
	        int month = resultSet.getInt("Month");
	        int count = resultSet.getInt("Count");

	        SimpleDateFormat sdf = new SimpleDateFormat("MMMM");
	        Calendar calendar = Calendar.getInstance();
	        calendar.set(Calendar.MONTH, month - 1);
	        String monthName = sdf.format(calendar.getTime());
	        json.put(monthName, count);
	    }

	    return json;
	}


	public JSONObject getApplicantsStatusGraph(int org_Id) throws SQLException, JSONException {
		
	    ConnectionClass db = ConnectionClass.CreateCon();
	    Connection connection = db.getConnection();
	    JSONObject json = new JSONObject();
	    
	    PreparedStatement preparedStatement = connection.prepareStatement(Constants.getApplicantsStatusGraph);
	    preparedStatement.setInt(1, org_Id);
	    ResultSet resultSet = preparedStatement.executeQuery();
	    
	    String[] status = {"Selected", "Rejected", "Onhold"};
	     for (String state : status) {
	    	 json.put(state, 0);
		 }
	    
	    while(resultSet.next()) {
	       	json.put(resultSet.getString("Status"), resultSet.getInt("Count"));
	    }
	    
	    return json;
	}
	
	public JSONObject selectedApplicantsGraphInDepartments(int orgId) throws SQLException, JSONException {
	    ConnectionClass db = ConnectionClass.CreateCon();
	    Connection connection = db.getConnection();
	    JSONObject json = new JSONObject();
	    
	    PreparedStatement preparedStatement = connection.prepareStatement(Constants.selectedApplicantsGraphInDepartments);
	    preparedStatement.setInt(1, orgId);
	    ResultSet resultSet = preparedStatement.executeQuery();
	    
	    while(resultSet.next()) {
	        String departmentTitle = resultSet.getString("Departments.Title");
	        int count = resultSet.getInt("count(*)");
	        if (count == 0) {
	            json.put(departmentTitle, 0);
	        } else {
	            json.put(departmentTitle, count);
	        }
	    }
	    
	    return json;
	}
	
	public JSONObject selectedApplicantsGraphInMonth(int orgId) throws SQLException, JSONException {
		
		 ConnectionClass db = ConnectionClass.CreateCon();
		 Connection connection = db.getConnection();
		 JSONObject json = new JSONObject();
		    
		 PreparedStatement preparedStatement = connection.prepareStatement(Constants.selectedApplicantsGraphInMonth);
	     preparedStatement.setInt(1, orgId);
	     ResultSet resultSet = preparedStatement.executeQuery();
		    
	    
		    
	     String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	     for (String monthName : monthNames) {
	    	 json.put(monthName, 0);
		 }

	     while (resultSet.next()) {
	    	 SimpleDateFormat sdf = new SimpleDateFormat("MMMM");
	    	 Calendar calendar = Calendar.getInstance();
		     calendar.set(Calendar.MONTH, resultSet.getInt("month") - 1);
		     String monthName = sdf.format(calendar.getTime());       
	   	     json.put(monthName, resultSet.getInt("count(*)"));
		 }
	     
	     return json;

	}
	
	public JSONObject differenceInYearByOpenings(int orgId) throws SQLException, JSONException {
		
		ConnectionClass db = ConnectionClass.CreateCon();
		Connection connection = db.getConnection();
		
		PreparedStatement preparedStatement = connection.prepareStatement(Constants.differenceInYearByOpenings);
		preparedStatement.setInt(1, orgId);
		ResultSet resultSet = preparedStatement.executeQuery();
		
		JSONObject jsonObject = new JSONObject();
		
		 int current = 0;
         int previous = 0;

         while (resultSet.next()) {
             int year = resultSet.getInt("year");
             int count = resultSet.getInt("count");
             
             if (year == java.time.LocalDate.now().getYear()) {
                 current = count;
             } else if (year == java.time.LocalDate.now().getYear() - 1) {
                 previous = count;
             }
         }
         
         double percentage = 0;
         if(previous != 0) {
        	 percentage = ((double) (current - previous) / previous) * 100;
         }
         
         jsonObject.put("percentage", percentage+"%");
         jsonObject.put("current", current);
         return jsonObject;
	}	
	
	
	public JSONObject differenceInMonthByHired(int orgId) throws SQLException, JSONException {
		
		ConnectionClass db = ConnectionClass.CreateCon();
		Connection connection = db.getConnection();
		
		PreparedStatement preparedStatement = connection.prepareStatement(Constants.differenceInMonthByHired);
		preparedStatement.setInt(1, orgId);
		ResultSet resultSet = preparedStatement.executeQuery();
		
		JSONObject jsonObject = new JSONObject();
		int current = 0;
        int previous = 0;
        while (resultSet.next()) {
            int month = resultSet.getInt("month");
            int count = resultSet.getInt("count");
            if (month == java.time.LocalDate.now().getMonthValue()) {
                current = count;
            } else if (month == java.time.LocalDate.now().minusMonths(1).getMonthValue()) {
                previous = count;
            }
        }
        
        double percentage = 0;
        if(previous != 0) {
       	 percentage = ((double) (current - previous) / previous) * 100;
        }
        jsonObject.put("percentage", percentage+"%");
        jsonObject.put("current", current);
        return jsonObject;
	}	
	
	
	public JSONArray seeApplicants(int openingId, String Status) throws SQLException, JSONException {
		
		ConnectionClass db = ConnectionClass.CreateCon();
		Connection connection = db.getConnection();
		JSONArray json = new JSONArray();
		
		PreparedStatement preparedStatement = connection.prepareStatement(Constants.getApplicants);
		preparedStatement.setString(1, Status);
		preparedStatement.setInt(2, openingId);
		ResultSet resultSet = preparedStatement.executeQuery();
		while(resultSet.next()) {

			int id = resultSet.getInt("Job_Seeker_Id");
	    	JSONObject details = new JSONObject();
	        details.put("id",id);
	        details.put("Name",resultSet.getString("Name"));
	        details.put("Email",resultSet.getString("Email"));
	        details.put("DOB",resultSet.getDate("DOB"));
	        details.put("Gender",resultSet.getString("Gender"));
	        details.put("Experience",resultSet.getInt("Experience"));
	        details.put("Phone",resultSet.getString("Phone"));
	        details.put("Qualification",resultSet.getString("Qualification")); 
//	        details.put("Photo",resultSet.getString("Photo"));
	        
			json.put(details);
		}
		
		return json;	
	}
	
}