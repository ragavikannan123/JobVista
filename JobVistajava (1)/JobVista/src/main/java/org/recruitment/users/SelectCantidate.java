package org.recruitment.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.recruitment.tests.Result;
import org.recruitment.tests.Test;
import org.util.ConnectionClass;
import org.util.Constants;

public class SelectCantidate {
	private static SelectCantidate thisSelectCantidate;
	private SelectCantidate() {
		
	}
	
	
	
	public  static SelectCantidate getSelectCantidate() {
		if (thisSelectCantidate == null) {
			thisSelectCantidate =  new SelectCantidate();
		}
		return thisSelectCantidate; 
	}
	
	
	public void selectCantidate(List<Result> result, Test test, int openingId, String selectedOnholdRejected) throws Exception {
		
		try {
		Connection conn = ConnectionClass.CreateCon().getConnection();
		int testId = 0;
		PreparedStatement selectTestId = conn.prepareStatement(Constants.testIdFromTileAndOpeningId);
		selectTestId.setString(1, test.getTitle());
		selectTestId.setInt(2, openingId);
		ResultSet testIdRs = selectTestId.executeQuery();
		
		
		if(testIdRs.next()) {
			testId = testIdRs.getInt(1);
		}else {
			throw new Exception("Test not Fount"); 
		}
		
//		INSERT INTO `Result` (`Job_Seeker_Id`, `Test_Id`, `Status`,`Points`) VALUES ()
		for( int i = 0; i < result.size(); i++) {
			Result currApplicant = result.get(i);
			
			PreparedStatement selectApplicant = conn.prepareStatement(Constants.applicantFromMailAndName);
			selectApplicant.setString(1, currApplicant.getApplicant().getName());
			selectApplicant.setString(2, currApplicant.getApplicant().getEmail());
			ResultSet applicantRS = selectApplicant.executeQuery();
			if(applicantRS.next()) {
				PreparedStatement giveresultStmt = conn.prepareStatement(Constants.giveResultToApplicant);
				giveresultStmt.setInt(1, applicantRS.getInt("Job_Seeker_Id"));
				giveresultStmt.setInt(2, testId);
				giveresultStmt.setString(2, selectedOnholdRejected);
				giveresultStmt.setInt(4, currApplicant.getpoints());
				giveresultStmt.executeQuery();
				
			}
		}
		
		
		
		
		}catch(SQLException e) {
			throw new Exception("Something unexpected happened. Please contact the admin.");
		}
	}
}
