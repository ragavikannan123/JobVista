package org.util;

public class Constants {

	
	
	
	//For panelist creating Opening
	public static String countOfOpenings= "Select count(*) as count From Openings;"; 
	public static String selectDepartmentandPanelist = "SELECT Department_Id FROM Panelist WHERE Panelist_Id = ?";


	public static String selectDepartmentAndOrgId = "SELECT Departments.Department_Id, Departments.Title AS DepartmentName, Organization.Org_Id, Organization.Name AS OrganizationName FROM Departments JOIN Organization ON Departments.Org_Id = Organization.Org_Id WHERE Departments.Title = ? AND Organization.Name = ?";
	public static String selextPanelistFromDepartOrg = "SELECT Panelist_Id, Name, Email, Gender, Position, Department_Id, Org_Id FROM Panelist WHERE Name like ? AND Gender like ? AND Email like ? AND Position like ? AND Org_Id = ? AND Department_Id = ?";

	public static String insertIntoOpenings = "INSERT INTO Openings (Opening_Id, Department_Id, Description, Experience, Qualification, Departments, EmploymentType, SalaryRange, Panelist_Id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public static String assignPanelistToOpenings = "INSERT INTO OpeningAndPanelist (Panelist_Id, Opening_Id) VALUES ((SELECT Panelist_Id FROM Panelist WHERE Email like ?),(SELECT Opening_Id FROM Openings WHERE Opening_Id = ?))";
	
	
	
	
	// For Adding Test

	public static String selectCoutOfTemp = "SELECT COUNT(*) AS NumberOfTemplates FROM `Template` WHERE Opening_Id = ? GROUP BY Opening_Id";

	public static String addNewTempToOpening = "INSERT INTO `Template` (`Opening_Id`, `TypeOfTest`, `RoundOn`) VALUES (?, ?, ?);";
	public static String addNewTestOpening = "INSERT INTO `Test` (`Opening_Id`, `Date`, `Title`, `Duration`, `TemplateId`) VALUES (?, ?, ?, ?, (select Template_Id from `Template` where `Opening_Id` = ? and `RoundOn` = ?))";

	public static String updateStartDateInOpening = "UPDATE `Openings` SET `Start_Date` = ? WHERE `Opening_Id` = ?";

	// for choosing a candidate
	public static String testIdFromTileAndOpeningId = "SELECT Test_Id FROM `Test` WHERE `Title` like ? and `Opening_Id` = ?";
	public static String applicantFromMailAndName = "SELECT * FROM `Job_Seeker` WHERE `Name`= ? and `Email` = ?";

	public static String giveResultToApplicant = "INSERT INTO `Result` (`Job_Seeker_Id`, `Test_Id`, `Status`,`Points`) VALUES (?, ?, ?, ?)";

	// For admin

	// For sign up

	public static String addAdmin = "insert into Admin (Name, Email, Org_Id) values (?, ?, ?)";

	public static String addOrganization = "insert into Organization (Name, TypeOfOrg, Industry, ContactEmail, ContactNumber) values (?, ?, ?, ?, ?)";
	public static String getOrgId = "select Org_Id from Organization where Name = ?";

	// For panelist management

	public static String isPanelistExists = "select * from Panelist where Email = ? and Org_Id = ?";
	public static String addPanelist = "insert into Panelist (Name, Email, Gender, Department_Id, Org_Id, Position) values (?, ?, ?, ?, ?, ?)";
	public static String addUser = "insert into Users (Role, Password, Email, Session_Id) values (?, ?, ?, ?)";
	public static String getDepartment = "select Department_Id from Departments where Title = ?";

	public static String removePanelist = "delete from Panelist where Panelist_Id = ?";

	public static String getPanelist = "select Panelist.*, Departments.Title from Panelist join Departments on Departments.Department_Id = Panelist.Department_Id where Departments.Org_Id = ?";

	public static String getPanelistWithDepartment = "select Panelist.*, Departments.Title from Panelist join Departments on Departments.Department_Id = Panelist.Department_Id where Departments.Org_Id = ? and Departments.Department_Id = ?";

	public static String getPanelistWithName = "select Panelist.*, Departments.Title from Panelist join Departments on Departments.Department_Id = Panelist.Department_Id where Panelist_Id = ?";

	public static String editPanelist = "update Panelist set Name=?, Email=?, Position=? where Panelist_Id = ?";

	// For department

	public static String isDepartmentExists = "select * from Departments where  title = ? and Org_Id = ?";
	public static String addDepartment = "insert into Departments (Title, Org_Id, Description) values (?, ?, ?)";

	public static String removeDepartment = "delete from Departments where Department_Id = ?";

	public static String getDepartments = "select * from Departments where Org_Id = ?";

	// For openings

	public static String getOpenings = "select Openings.*, Panelist.* from Openings join Panelist on Panelist.Panelist_Id = Openings.Panelist_Id join Departments on Departments.Department_Id = Panelist.Department_Id where Departments.Org_Id = ?";

	public static String getOpeningsWithDepartment = "select Openings.*, Panelist.* from Openings join Panelist on Panelist.Panelist_Id = Openings.Panelist_Id where Panelist.Department_Id = ?";

//	public static String getCurrentOpenings = "SELECT Openings.*, Panelist.Name FROM Openings JOIN Panelist ON Panelist.Panelist_Id = Openings.Panelist_Id JOIN Departments ON Departments.Department_Id = Panelist.Department_Id WHERE Departments.Org_Id = ? AND Openings.Start_Date >= ?;";

	// For graph

//	public static String getOpeningGraphWithDepartments = "select Departments.Title as DepartmentTitle, count(*) as Count from Openings join Departments on Openings.Department_Id = Departments.Department_Id where Departments.Org_Id = ? group by Departments.Title";

//	public static String getOpeningGraphByMonth = "select month(Start_Date) as Month, count(*) as Count from Openings join Departments on Openings.Department_Id = Departments.Department_Id where Departments.Org_Id = ? and Openings.Start_Date >= date_sub(curdate(), interval 1 year) group by month(Start_Date)";


//	public static String getApplicantsStatusGraph = "select Status, count(*) as count from Result join Test on Test.Test_Id = Result.Test_Id join Openings on Openings.Opening_Id = Test.Opening_Id join Departments on Departments.Department_Id = Openings.Department_Id where Departments.Org_Id = ? group by Status";

//	public static String selectedApplicantsGraphInDepartments = "select Departments.Title, count(*) from Result join Test on Test.Test_Id = Result.Test_Id join Openings on Openings.Opening_Id = Test.Opening_Id join Departments on Departments.Department_Id = Openings.Department_Id where Departments.Org_Id = ? and Result.Status = 'Selected' Group by Departments.Title";

//	public static String selectedApplicantsGraphInMonth = "select month(Openings.Start_Date) as month, count(*) from Result join Test on Test.Test_Id = Result.Test_Id join Openings on Openings.Opening_Id = Test.Opening_Id join Departments on Departments.Department_Id = Openings.Department_Id where Departments.Org_Id = ? and Result.Status = 'Selected' and Openings.Start_Date >= date_sub(curdate(), interval 1 year) Group by month(Openings.Start_Date)";

	public static String differenceInMonthByHired = "select month(Openings.Start_Date) as month, count(*) as count from Result join Test on Test.Test_Id = Result.Test_Id join Openings on Openings.Opening_Id = Test.Opening_Id join Departments on Departments.Department_Id = Openings.Department_Id where Departments.Org_Id = ? and Result.Status = 'Selected' and month(Openings.Start_Date) in (month(current_date()), month(current_date()) - 1) group by month(Openings.Start_Date)";

	public static String differenceInYearByOpenings = "select year(Openings.Start_Date) as year, count(*) as count from Openings join Departments on Departments.Department_Id = Openings.Department_Id where Departments.Org_Id = ? and year(Openings.Start_Date) in (year(current_date()), year(current_date()) - 1) group by year(Start_Date)";

		// For applicants

	public static String getApplicants = "SELECT * FROM Job_Seeker JOIN Result ON Job_Seeker.Job_Seeker_Id = Result.Job_Seeker_id JOIN Test ON Test.Test_Id = Result.Test_Id WHERE Result.Status = ? AND Test.Opening_Id = ?;";

	public static String checkUser = "select * from Users where Email = ? and Password = ?";

	public static String updateSession = "update Users set Session_Id = ? where Email = ?";

	public static String OrgId = "select Org_Id from Admin where Email = ?";
	
	public static String OrgIdForPanelist = "select Departments.Org_Id from Departments join Panelist on Departments.Department_Id = Panelist.Department_Id where Panelist.Email = ?";
	
	public static String panelistId = "select Panelist_Id from Panelist where Email = ?";

	public static String adminId = "select Admin_Id from Admin where Email = ?";
	
	public static String getCurrentOpenings = "SELECT Openings.*, Panelist.Name FROM Openings JOIN Panelist ON Panelist.Panelist_Id = Openings.Panelist_Id JOIN Departments ON Departments.Department_Id = Panelist.Department_Id WHERE Departments.Org_Id = ? AND Openings.Start_Date >= ?";
	// For graph

	public static String getOpeningGraphWithDepartments = "select Departments.Title as DepartmentTitle, count(*) as Count from Openings join Departments on Openings.Department_Id = Departments.Department_Id where Departments.Org_Id = ? group by Departments.Title";

	public static String getOpeningGraphByMonth = "select month(Start_Date) as Month, count(*) as Count from Openings join Departments on Openings.Department_Id = Departments.Department_Id where Departments.Org_Id = ? and Openings.Start_Date >= date_sub(curdate(), interval 1 year) group by month(Start_Date)";

	public static String getApplicantsStatusGraph = "select Status, count(*) as count from Result join Test on Test.Test_Id = Result.Test_Id join Openings on Openings.Opening_Id = Test.Opening_Id join Departments on Departments.Department_Id = Openings.Department_Id where Departments.Org_Id = ? group by Status";

	public static String selectedApplicantsGraphInDepartments = "select Departments.Title, count(*) from Result join Test on Test.Test_Id = Result.Test_Id join Openings on Openings.Opening_Id = Test.Opening_Id join Departments on Departments.Department_Id = Openings.Department_Id where Departments.Org_Id = ? and Result.Status = 'Selected' Group by Departments.Title";

	public static String selectedApplicantsGraphInMonth = "select month(Openings.Start_Date) as month, count(*) from Result join Test on Test.Test_Id = Result.Test_Id join Openings on Openings.Opening_Id = Test.Opening_Id join Departments on Departments.Department_Id = Openings.Department_Id where Departments.Org_Id = ? and Result.Status = 'Selected' and Openings.Start_Date >= date_sub(curdate(), interval 1 year) Group by month(Openings.Start_Date)";
}
