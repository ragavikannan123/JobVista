package org.recruitment.tests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Date;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.util.CommonLogger;
import org.util.ConnectionClass;
import org.util.Constants;

public class TestManager {
	
	
private static TestManager thisTestManager;
	Logger log;
	
	private TestManager() {
		log = CommonLogger.getCommon().getLogger(TestManager.class);
	}
	
	public static TestManager getTestManager() {
		if(thisTestManager == null) {
			thisTestManager = new TestManager();
			
		}
		return thisTestManager;
	}
	
	
	public void addTest(int openingId, Test test) throws Exception {
		System.out.println("intest");
		try {
		Connection conn = ConnectionClass.CreateCon().getConnection();
		
		
		int count = 1;
		PreparedStatement countTempStatement = conn.prepareStatement(Constants.selectCoutOfTemp);
		countTempStatement.setInt(1, openingId);
		ResultSet coutOfTemp = countTempStatement.executeQuery();
		
		
		if(coutOfTemp.next()) {
			count = coutOfTemp.getInt(1) + 1;
		}
		
		
		System.out.println(test.getTypeOfTest());
		PreparedStatement assignTemplates = conn.prepareStatement(Constants.addNewTempToOpening);
		assignTemplates.setInt(1, openingId);
		assignTemplates.setString(2, test.getTypeOfTest());
		assignTemplates.setInt(3, count);
		
		assignTemplates.executeUpdate();
		
		
		Date date = test.getDate();
		System.out.println(date);
		
		java.sql.Date sqlDate = new java.sql.Date(test.getDate().getTime());
		
		PreparedStatement assgnTest = conn.prepareStatement(Constants.addNewTestOpening);
		assgnTest.setInt(1, openingId);
		assgnTest.setDate(2, sqlDate);
		assgnTest.setString(3, test.getTitle());
		assgnTest.setInt(4, test.getDuration());
		assgnTest.setInt(5, openingId);
		assgnTest.setInt(6, count);
		assgnTest.executeUpdate();
		
		
		
		if(count == 1) {
			PreparedStatement updateDateInOpening = conn.prepareStatement(Constants.updateStartDateInOpening);
			updateDateInOpening.setDate(1, sqlDate);
			updateDateInOpening.setInt(2, openingId);
			updateDateInOpening.executeUpdate();
		}
		
		
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new Exception(e.getMessage());
		}
		
	}
	
}
