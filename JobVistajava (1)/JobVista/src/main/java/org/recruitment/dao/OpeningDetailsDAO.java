package org.recruitment.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.StyledEditorKit.BoldAction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.util.ConnectionClass;

public class OpeningDetailsDAO<E> {

	public List<JSONObject> getAllUpComingOpenings() {
		List<JSONObject> openingDetailsList = new ArrayList<>();
		try (Connection connection = ConnectionClass.CreateCon().getConnection()) {
			String query = "SELECT \n" + "    o.Opening_Id AS Opening_Id,\n"
					+ "    o.Department_Id AS Opening_Department_Id,\n" + "    o.Description AS Opening_Description,\n"
					+ "    o.Experience AS Opening_Experience,\n" + "    o.Qualification AS Opening_Qualification,\n"
					+ "    o.Departments AS Opening_Departments,\n"
					+ "    o.EmploymentType AS Opening_EmploymentType,\n"
					+ "    o.SalaryRange AS Opening_SalaryRange,\n" + "    o.Panelist_Id AS Opening_Panelist_Id,\n"
					+ "    o.Start_Date AS Opening_Start_Date,\n" + "    o.End_Date AS Opening_End_Date,\n"
					+ "    p.Panelist_Id AS Panelist_Panelist_Id,\n" + "    p.Name AS Panelist_Name,\n"
					+ "    p.Email AS Panelist_Email,\n" + "    p.Gender AS Panelist_Gender,\n"
					+ "    p.Department_Id AS Panelist_Department_Id,\n" + "    p.Org_Id AS Panelist_Org_Id,\n"
					+ "    p.Position AS Panelist_Position,\n" + "    d.Department_Id AS Department_Department_Id,\n"
					+ "    d.Title AS Department_Title,\n" + "    d.Org_Id AS Department_Org_Id,\n"
					+ "    org1.Org_Id AS Org1_Id,\n" + "    org1.Name AS Org1_Name,\n"
					+ "    org1.TypeOfOrg AS Org1_TypeOfOrg,\n" + "    org1.Industry AS Org1_Industry,\n"
					+ "    org1.ContactEmail AS Org1_ContactEmail,\n"
					+ "    org1.ContactNumber AS Org1_ContactNumber,\n" + "    org2.Org_Id AS Org2_Id,\n"
					+ "    org2.Name AS Org2_Name,\n" + "    org2.TypeOfOrg AS Org2_TypeOfOrg,\n"
					+ "    org2.Industry AS Org2_Industry,\n" + "    org2.ContactEmail AS Org2_ContactEmail,\n"
					+ "    org2.ContactNumber AS Org2_ContactNumber\n" + "FROM Openings o\n"
					+ "JOIN Panelist p ON o.Panelist_Id = p.Panelist_Id\n"
					+ "JOIN Departments d ON o.Department_Id = d.Department_Id\n"
					+ "JOIN Organization org1 ON d.Org_Id = org1.Org_Id\n"
					+ "JOIN Organization org2 ON p.Org_Id = org2.Org_Id\n"
					+ "WHERE o.Start_Date IS NOT NULL AND o.Start_Date >= CURDATE();\n";
			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					while (resultSet.next()) {
						JSONObject openingDetailsJSON;
						try {
							openingDetailsJSON = mapResultSetToJSON(resultSet);
							openingDetailsList.add(openingDetailsJSON);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return openingDetailsList;
	}

	private JSONObject mapResultSetToJSON(ResultSet resultSet) throws SQLException, JSONException {
		JSONObject openingDetailsJSON = new JSONObject();
		openingDetailsJSON.put("Opening", getOpeningDetails(resultSet));
		openingDetailsJSON.put("Panelist", getPanelistDetails(resultSet));
		openingDetailsJSON.put("Organization", getOrganizationDetails(resultSet));
		openingDetailsJSON.put("Department", getDepartmentDetails(resultSet));
		return openingDetailsJSON;
	}

	public List<JSONObject> getAllOpeningsForAdmin(int OrgID) {
		List<JSONObject> openingDetailsList = new ArrayList<>();
		try (Connection connection = ConnectionClass.CreateCon().getConnection()) {
			String query = "SELECT o.Opening_Id AS Opening_Id, o.Department_Id AS Opening_Department_Id, "
					+ "o.Description AS Opening_Description, o.Experience AS Opening_Experience, "
					+ "o.Qualification AS Opening_Qualification, o.Departments AS Opening_Departments, "
					+ "o.EmploymentType AS Opening_EmploymentType, o.SalaryRange AS Opening_SalaryRange, "
					+ "o.Panelist_Id AS Opening_Panelist_Id, o.Start_Date AS Opening_Start_Date, "
					+ "o.End_Date AS Opening_End_Date, p.Panelist_Id AS Panelist_Panelist_Id, "
					+ "p.Name AS Panelist_Name, p.Email AS Panelist_Email, p.Gender AS Panelist_Gender, "
					+ "p.Department_Id AS Panelist_Department_Id, p.Org_Id AS Panelist_Org_Id, "
					+ "p.Position AS Panelist_Position, d.Department_Id AS Department_Department_Id, "
					+ "d.Title AS Department_Title, d.Org_Id AS Department_Org_Id, org1.Org_Id AS Org1_Id, "
					+ "org1.Name AS Org1_Name, org1.TypeOfOrg AS Org1_TypeOfOrg, org1.Industry AS Org1_Industry, "
					+ "org1.ContactEmail AS Org1_ContactEmail, org1.ContactNumber AS Org1_ContactNumber, "
					+ "org2.Org_Id AS Org2_Id, org2.Name AS Org2_Name, org2.TypeOfOrg AS Org2_TypeOfOrg, "
					+ "org2.Industry AS Org2_Industry, org2.ContactEmail AS Org2_ContactEmail, "
					+ "org2.ContactNumber AS Org2_ContactNumber " + "FROM Openings o "
					+ "JOIN Panelist p ON o.Panelist_Id = p.Panelist_Id "
					+ "JOIN Departments d ON o.Department_Id = d.Department_Id "
					+ "JOIN Organization org1 ON d.Org_Id = org1.Org_Id "
					+ "JOIN Organization org2 ON p.Org_Id = org2.Org_Id "
					+ "WHERE (org1.Org_Id = ? OR org2.Org_Id = ?)";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, OrgID);
			preparedStatement.setInt(2, OrgID);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					JSONObject openingDetailsJSON;
					try {
						openingDetailsJSON = mapResultSetToJSONForAdmin(resultSet);
						openingDetailsList.add(openingDetailsJSON);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return openingDetailsList;
	}

	private JSONObject mapResultSetToJSONForAdmin(ResultSet resultSet) throws SQLException, JSONException {
		JSONObject openingDetailsJSON = new JSONObject();
		openingDetailsJSON.put("Opening", getOpeningDetails(resultSet));
		openingDetailsJSON.put("CreatedPanelist", getPanelistDetails(resultSet));
		openingDetailsJSON.put("Organization", getOrganizationDetails(resultSet));
		openingDetailsJSON.put("Department", getDepartmentDetails(resultSet));
		openingDetailsJSON.put("PanelistsinOpening", getPanelistDetailsByOpeningId(resultSet.getLong("Opening_Id")));
		openingDetailsJSON.put("Template", getTemplateDetailsByOpeningId(resultSet.getLong("Opening_Id")));
		openingDetailsJSON.put("Tests", getTestsDetailsByOpeningId(resultSet.getLong("Opening_Id")));
		openingDetailsJSON.put("Applicants", getApplicantsDetailsByOpeningId(resultSet.getLong("Opening_Id")));
		return openingDetailsJSON;
	}

	public List<JSONObject> getOpeningsForPanelist(int PanelistID, List<JSONObject> openingDetailsList) {
		List<JSONObject> openingDetailsListJSON = new ArrayList<>();
//		JSONObject openingDetailsJSON = new JSONObject();
		try (Connection connection = ConnectionClass.CreateCon().getConnection()) {
			String query = "SELECT o.Opening_Id AS Opening_Id, o.Department_Id AS Opening_Department_Id, "
					+ "o.Description AS Opening_Description, o.Experience AS Opening_Experience, "
					+ "o.Qualification AS Opening_Qualification, o.Departments AS Opening_Departments, "
					+ "o.EmploymentType AS Opening_EmploymentType, o.SalaryRange AS Opening_SalaryRange, "
					+ "o.Panelist_Id AS Opening_Panelist_Id, o.Start_Date AS Opening_Start_Date, "
					+ "o.End_Date AS Opening_End_Date, p.Panelist_Id AS Panelist_Panelist_Id, "
					+ "p.Name AS Panelist_Name, p.Email AS Panelist_Email, p.Gender AS Panelist_Gender, "
					+ "p.Department_Id AS Panelist_Department_Id, p.Org_Id AS Panelist_Org_Id, "
					+ "p.Position AS Panelist_Position, d.Department_Id AS Department_Department_Id, "
					+ "d.Title AS Department_Title, d.Org_Id AS Department_Org_Id, org1.Org_Id AS Org1_Id, "
					+ "org1.Name AS Org1_Name, org1.TypeOfOrg AS Org1_TypeOfOrg, org1.Industry AS Org1_Industry, "
					+ "org1.ContactEmail AS Org1_ContactEmail, org1.ContactNumber AS Org1_ContactNumber, "
					+ "org2.Org_Id AS Org2_Id, org2.Name AS Org2_Name, org2.TypeOfOrg AS Org2_TypeOfOrg, "
					+ "org2.Industry AS Org2_Industry, org2.ContactEmail AS Org2_ContactEmail, "
					+ "org2.ContactNumber AS Org2_ContactNumber " + "FROM Openings o "
					+ "JOIN Panelist p ON o.Panelist_Id = p.Panelist_Id "
					+ "JOIN Departments d ON o.Department_Id = d.Department_Id "
					+ "JOIN Organization org1 ON d.Org_Id = org1.Org_Id "
					+ "JOIN Organization org2 ON p.Org_Id = org2.Org_Id " + "WHERE (p.Panelist_Id = ?)";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, PanelistID);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {

				while (resultSet.next()) {
					JSONObject openingDetailsJSONForGettingDetails = new JSONObject();
					try {
						openingDetailsJSONForGettingDetails = mapResultSetToJSONForPanelist(resultSet,
								openingDetailsListJSON);
//						openingDetailsList.add(openingDetailsJSONForGettingDetails);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
//				openingDetailsList.put("openings", openingDetailsListJSON);
//			} catch (JSONException e1) {
//				e1.printStackTrace();
//			}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return openingDetailsListJSON;
	}

	private JSONObject mapResultSetToJSONForPanelist(ResultSet resultSet, List<JSONObject> openingDetailsList)
			throws SQLException, JSONException {
		JSONObject openingDetailsJSON = new JSONObject();
		openingDetailsList.add(getOpeningDetailsForPanelist(resultSet));
		return openingDetailsJSON;
	}

	public List<JSONObject> getCurrentOpeningsForPanelist(int panelistID, List<JSONObject> openingDetailsList) {
		List<JSONObject> openingDetailsListJSON = new ArrayList<>();
//	    JSONObject openingDetailsJSON = new JSONObject();

		try (Connection connection = ConnectionClass.CreateCon().getConnection()) {
			String query = "SELECT o.Opening_Id AS Opening_Id, o.Department_Id AS Opening_Department_Id, "
					+ "o.Description AS Opening_Description, o.Experience AS Opening_Experience, "
					+ "o.Qualification AS Opening_Qualification, o.Departments AS Opening_Departments, "
					+ "o.EmploymentType AS Opening_EmploymentType, o.SalaryRange AS Opening_SalaryRange, "
					+ "o.Panelist_Id AS Opening_Panelist_Id, o.Start_Date AS Opening_Start_Date, "
					+ "o.End_Date AS Opening_End_Date, p.Panelist_Id AS Panelist_Panelist_Id, "
					+ "p.Name AS Panelist_Name, p.Email AS Panelist_Email, p.Gender AS Panelist_Gender, "
					+ "p.Department_Id AS Panelist_Department_Id, p.Org_Id AS Panelist_Org_Id, "
					+ "p.Position AS Panelist_Position, d.Department_Id AS Department_Department_Id, "
					+ "d.Title AS Department_Title, d.Org_Id AS Department_Org_Id, org1.Org_Id AS Org1_Id, "
					+ "org1.Name AS Org1_Name, org1.TypeOfOrg AS Org1_TypeOfOrg, org1.Industry AS Org1_Industry, "
					+ "org1.ContactEmail AS Org1_ContactEmail, org1.ContactNumber AS Org1_ContactNumber, "
					+ "org2.Org_Id AS Org2_Id, org2.Name AS Org2_Name, org2.TypeOfOrg AS Org2_TypeOfOrg, "
					+ "org2.Industry AS Org2_Industry, org2.ContactEmail AS Org2_ContactEmail, "
					+ "org2.ContactNumber AS Org2_ContactNumber " + "FROM Openings o "
					+ "JOIN Panelist p ON o.Panelist_Id = p.Panelist_Id "
					+ "JOIN Departments d ON o.Department_Id = d.Department_Id "
					+ "JOIN Organization org1 ON d.Org_Id = org1.Org_Id "
					+ "JOIN Organization org2 ON p.Org_Id = org2.Org_Id "
					+ "WHERE (p.Panelist_Id = ?) AND (CURRENT_DATE BETWEEN o.Start_Date AND COALESCE(o.End_Date, CURRENT_DATE));";

			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				preparedStatement.setInt(1, panelistID);

				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					while (resultSet.next()) {
						JSONObject openingDetailsJSONForGettingDetails = new JSONObject();
						try {
							openingDetailsJSONForGettingDetails = mapResultSetToJSONForPanelist(resultSet,
									openingDetailsListJSON);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
//					openingDetailsList.put("openings", openingDetailsListJSON);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
			}

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return openingDetailsListJSON;
	}

	public List<JSONObject> getUpComingOpeningsForPanelist(int panelistID, List<JSONObject> openingDetailsList) {
		List<JSONObject> openingDetailsListJSON = new ArrayList<>();
//	    JSONObject openingDetailsJSON = new JSONObject();

		try (Connection connection = ConnectionClass.CreateCon().getConnection()) {
			String query = "SELECT o.Opening_Id AS Opening_Id, o.Department_Id AS Opening_Department_Id, "
					+ "o.Description AS Opening_Description, o.Experience AS Opening_Experience, "
					+ "o.Qualification AS Opening_Qualification, o.Departments AS Opening_Departments, "
					+ "o.EmploymentType AS Opening_EmploymentType, o.SalaryRange AS Opening_SalaryRange, "
					+ "o.Panelist_Id AS Opening_Panelist_Id, o.Start_Date AS Opening_Start_Date, "
					+ "o.End_Date AS Opening_End_Date, p.Panelist_Id AS Panelist_Panelist_Id, "
					+ "p.Name AS Panelist_Name, p.Email AS Panelist_Email, p.Gender AS Panelist_Gender, "
					+ "p.Department_Id AS Panelist_Department_Id, p.Org_Id AS Panelist_Org_Id, "
					+ "p.Position AS Panelist_Position, d.Department_Id AS Department_Department_Id, "
					+ "d.Title AS Department_Title, d.Org_Id AS Department_Org_Id, org1.Org_Id AS Org1_Id, "
					+ "org1.Name AS Org1_Name, org1.TypeOfOrg AS Org1_TypeOfOrg, org1.Industry AS Org1_Industry, "
					+ "org1.ContactEmail AS Org1_ContactEmail, org1.ContactNumber AS Org1_ContactNumber, "
					+ "org2.Org_Id AS Org2_Id, org2.Name AS Org2_Name, org2.TypeOfOrg AS Org2_TypeOfOrg, "
					+ "org2.Industry AS Org2_Industry, org2.ContactEmail AS Org2_ContactEmail, "
					+ "org2.ContactNumber AS Org2_ContactNumber " + "FROM Openings o "
					+ "JOIN Panelist p ON o.Panelist_Id = p.Panelist_Id "
					+ "JOIN Departments d ON o.Department_Id = d.Department_Id "
					+ "JOIN Organization org1 ON d.Org_Id = org1.Org_Id "
					+ "JOIN Organization org2 ON p.Org_Id = org2.Org_Id "
					+ "WHERE (p.Panelist_Id = ?) AND (o.Start_Date > CURRENT_DATE)";

			try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				preparedStatement.setInt(1, panelistID);

				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					while (resultSet.next()) {
						JSONObject openingDetailsJSONForGettingDetails = new JSONObject();
						try {
							openingDetailsJSONForGettingDetails = mapResultSetToJSONForPanelist(resultSet,
									openingDetailsListJSON);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
//					openingDetailsList.put("openings", openingDetailsListJSON);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} catch (SQLException e2) {
				e2.printStackTrace();
			}

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return openingDetailsListJSON;
	}

	private JSONObject getOpeningDetails(ResultSet resultSet) throws SQLException, JSONException {
		JSONObject openingJSON = new JSONObject();

		openingJSON.put("openingId", resultSet.getLong("Opening_Id"));
		openingJSON.put("description", resultSet.getString("Opening_Description"));
		openingJSON.put("experience", resultSet.getInt("Opening_Experience"));
		openingJSON.put("qualification", resultSet.getString("Opening_Qualification"));
		openingJSON.put("departments", resultSet.getString("Opening_Departments"));
		openingJSON.put("employmentType", resultSet.getString("Opening_EmploymentType"));
		openingJSON.put("salaryRange", resultSet.getString("Opening_SalaryRange"));
		openingJSON.put("startDate", resultSet.getDate("Opening_Start_Date"));
		openingJSON.put("endDate", resultSet.getDate("Opening_End_Date"));

		return openingJSON;
	}

	private JSONObject getOpeningDetailsForPanelist(ResultSet resultSet) throws SQLException, JSONException {
		JSONObject openingJSON = new JSONObject();

		openingJSON.put("openingId", resultSet.getLong("Opening_Id"));
		openingJSON.put("description", resultSet.getString("Opening_Description"));
		openingJSON.put("experience", resultSet.getInt("Opening_Experience"));
		openingJSON.put("qualification", resultSet.getString("Opening_Qualification"));
		openingJSON.put("departments", resultSet.getString("Opening_Departments"));
		openingJSON.put("employmentType", resultSet.getString("Opening_EmploymentType"));
		openingJSON.put("salaryRange", resultSet.getString("Opening_SalaryRange"));
		openingJSON.put("startDate", resultSet.getDate("Opening_Start_Date"));
		openingJSON.put("endDate", resultSet.getDate("Opening_End_Date"));
		openingJSON.put("applicants", getApplicantsDetailsByOpeningId(resultSet.getLong("Opening_Id")));
		openingJSON.put("Panelist", getPanelistDetails(resultSet));
		openingJSON.put("Interviewers", getPanelistDetailsByOpeningId(resultSet.getLong("Opening_Id")));
		openingJSON.put("test", getTestsDetailsByOpeningIdForPanelist(resultSet.getLong("Opening_Id")));

		return openingJSON;
	}

	private JSONObject getPanelistDetails(ResultSet resultSet) throws SQLException, JSONException {
		JSONObject panelistJSON = new JSONObject();

		panelistJSON.put("panelistId", resultSet.getLong("Panelist_Panelist_Id"));
		panelistJSON.put("panelistName", resultSet.getString("Panelist_Name"));
		panelistJSON.put("panelistEmail", resultSet.getString("Panelist_Email"));
		panelistJSON.put("panelistGender", resultSet.getString("Panelist_Gender"));
		panelistJSON.put("panelistDepartmentId", resultSet.getLong("Panelist_Department_Id"));
		panelistJSON.put("panelistOrgId", resultSet.getLong("Panelist_Org_Id"));
		panelistJSON.put("panelistPosition", resultSet.getString("Panelist_Position"));

		return panelistJSON;
	}

	private JSONObject getOrganizationDetails(ResultSet resultSet) throws SQLException, JSONException {
		JSONObject organizationJSON = new JSONObject();

		organizationJSON.put("orgId", resultSet.getLong("Org1_Id"));
		organizationJSON.put("orgName", resultSet.getString("Org1_Name"));
		organizationJSON.put("orgTypeOfOrg", resultSet.getString("Org1_TypeOfOrg"));
		organizationJSON.put("orgIndustry", resultSet.getString("Org1_Industry"));
		organizationJSON.put("orgContactEmail", resultSet.getString("Org1_ContactEmail"));
		organizationJSON.put("orgContactNumber", resultSet.getString("Org1_ContactNumber"));
		return organizationJSON;
	}

	private JSONObject getDepartmentDetails(ResultSet resultSet) throws SQLException, JSONException {
		JSONObject departmentJSON = new JSONObject();

		departmentJSON.put("departmentId", resultSet.getLong("Department_Department_Id"));
		departmentJSON.put("departmentTitle", resultSet.getString("Department_Title"));
		departmentJSON.put("departmentOrgId", resultSet.getLong("Department_Org_Id"));

		return departmentJSON;
	}

	private JSONArray getPanelistDetailsByOpeningId(long openingId) throws SQLException, JSONException {
		JSONArray panelistArray = new JSONArray();
		String panelistQuery = "SELECT p.* FROM Panelist p "
				+ "JOIN OpeningAndPanelist op ON p.Panelist_Id = op.Panelist_Id " + "WHERE op.Opening_Id = ?;";
		try (Connection connection = ConnectionClass.CreateCon().getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(panelistQuery)) {
			preparedStatement.setLong(1, openingId);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					panelistArray.put(mapAllPanelistResultSetToJSON(resultSet));
				}
			}
		}
		return panelistArray;
	}

	private JSONObject mapAllPanelistResultSetToJSON(ResultSet resultSet) throws SQLException, JSONException {
		JSONObject panelistJSON = new JSONObject();
		panelistJSON.put("panelistId", resultSet.getLong("Panelist_Id"));
		panelistJSON.put("panelistName", resultSet.getString("Name"));
		panelistJSON.put("panelistEmail", resultSet.getString("Email"));
		panelistJSON.put("panelistGender", resultSet.getString("Gender"));
		panelistJSON.put("panelistDepartmentId", resultSet.getLong("Department_Id"));
		panelistJSON.put("panelistOrgId", resultSet.getLong("Org_Id"));
		panelistJSON.put("panelistPosition", resultSet.getString("Position"));
		return panelistJSON;
	}

	public JSONObject getTestDetailsByOpeningId(int openingId) {
		String testQuery = "SELECT * FROM Test WHERE Opening_Id = ?";
		try (Connection connection = ConnectionClass.CreateCon().getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(testQuery)) {
			preparedStatement.setInt(1, openingId);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					return mapTestResultSetToJSON(resultSet);
				}
			}
		} catch (SQLException | JSONException e) {
			e.printStackTrace();
		}
		// If no test details found, return an empty JSON object
		return new JSONObject();
	}

	private JSONObject getTestDetails(ResultSet resultSet) throws SQLException, JSONException {
		JSONObject testJSON = new JSONObject();
		long testId = resultSet.getLong("Test_Id");
		if (!resultSet.wasNull()) {
			testJSON.put("testId", testId);
			testJSON.put("testDate", resultSet.getDate("Test_Date"));
			testJSON.put("testTitle", resultSet.getString("Test_Title"));
			testJSON.put("testDuration", resultSet.getInt("Test_Duration"));
		} else {
			testJSON.put("tesmapTestResultSetToJSONtId", JSONObject.NULL);
			testJSON.put("testDate", JSONObject.NULL);
			testJSON.put("testTitle", JSONObject.NULL);
			testJSON.put("testDuration", JSONObject.NULL);
		}
		return testJSON;
	}

	private JSONArray getTestsDetailsByOpeningId(long openingId) throws SQLException, JSONException {
		String testQuery = "SELECT * FROM Test WHERE Opening_Id = ?;";
		JSONArray testsArray = new JSONArray();
		try (Connection connection = ConnectionClass.CreateCon().getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(testQuery)) {
			preparedStatement.setLong(1, openingId);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					testsArray.put(mapTestResultSetToJSON(resultSet));
				}
			}
		}
		return testsArray;
	}

	private JSONArray getTestsDetailsByOpeningIdForPanelist(long openingId) throws SQLException, JSONException {
		String testQuery = "SELECT * FROM Test WHERE Opening_Id = ?;";
		JSONArray testsArray = new JSONArray();
		try (Connection connection = ConnectionClass.CreateCon().getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(testQuery)) {
			preparedStatement.setLong(1, openingId);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					testsArray.put(mapTestResultSetToJSONForPanelist(resultSet));
				}
			}
		}
		return testsArray;
	}

	private JSONObject getTestDetailsByOpeningId(long openingId) throws SQLException, JSONException {
		String testQuery = "SELECT * FROM Test WHERE Opening_Id = ?;";
		try (Connection connection = ConnectionClass.CreateCon().getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(testQuery)) {
			preparedStatement.setLong(1, openingId);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					return mapTestResultSetToJSON(resultSet);
				}
			}
		}
		return new JSONObject();
	}

	private JSONObject mapTestResultSetToJSON(ResultSet resultSet) throws SQLException, JSONException {
		JSONObject testJSON = new JSONObject();
		testJSON.put("testId", resultSet.getLong("Test_Id"));
		testJSON.put("testDate", resultSet.getDate("Date"));
		testJSON.put("testTitle", resultSet.getString("Title"));
		testJSON.put("testDuration", resultSet.getInt("Duration"));
		testJSON.put("TemplateId", resultSet.getInt("TemplateId"));

//		testJSON.put("Template", getTemplateDetailsByOpeningId(resultSet.getLong("Opening_Id")));
		return testJSON;
	}

	private JSONObject mapTestResultSetToJSONForPanelist(ResultSet resultSet) throws SQLException, JSONException {
		JSONObject testJSON = new JSONObject();
		testJSON.put("testId", resultSet.getLong("Test_Id"));
		testJSON.put("testDate", resultSet.getDate("Date"));
		testJSON.put("testTitle", resultSet.getString("Title"));
		testJSON.put("testDuration", resultSet.getInt("Duration"));

//		testJSON.put("TemplateId", resultSet.getInt("TemplateId"));
		boolean isFaceToFace = getTemplateDetailsByTemplateIdForPanelist(resultSet.getLong("TemplateId"), testJSON);
		testJSON.put("applicants", getApplicantsDetailsByTestId(resultSet.getLong("Test_Id"), isFaceToFace));
		return testJSON;
	}

	private boolean getTemplateDetailsByTemplateIdForPanelist(long Template_Id, JSONObject testJSON)
			throws SQLException, JSONException {
		String templateQuery = "SELECT * FROM Template WHERE Template_Id = ?;";
		try (Connection connection = ConnectionClass.CreateCon().getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(templateQuery)) {
			preparedStatement.setLong(1, Template_Id);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					return mapTemplateResultSetToJSONForPanelist(resultSet, testJSON);
				}
			}
		}
//		return templateArray;
		return false;
	}

	private JSONArray getTemplateDetailsByOpeningId(long openingId) throws SQLException, JSONException {
		JSONArray templateArray = new JSONArray();
		String templateQuery = "SELECT * FROM Template WHERE Template_Id IN "
				+ "(SELECT TemplateId FROM Test WHERE Opening_Id = ?);";
		try (Connection connection = ConnectionClass.CreateCon().getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(templateQuery)) {
			preparedStatement.setLong(1, openingId);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					templateArray.put(mapTemplateResultSetToJSON(resultSet));
				}
			}
		}
		return templateArray;
	}

	private boolean mapTemplateResultSetToJSONForPanelist(ResultSet resultSet, JSONObject testJSON)
			throws SQLException, JSONException {
		String typeofTest = resultSet.getString("TypeOfTest");
		testJSON.put("templateTypeOfTest", typeofTest);
		testJSON.put("templateRoundOn", resultSet.getInt("RoundOn"));
		if (typeofTest.equals("FaceToFace")) {
			return true;
		} else {
			return false;
		}
	}

	private JSONObject mapTemplateResultSetToJSON(ResultSet resultSet) throws SQLException, JSONException {
		JSONObject templateJSON = new JSONObject();
		templateJSON.put("templateId", resultSet.getLong("Template_Id"));
		templateJSON.put("templateTypeOfTest", resultSet.getString("TypeOfTest"));
		templateJSON.put("templateRoundOn", resultSet.getInt("RoundOn"));
		return templateJSON;
	}

	private JSONArray getApplicantsArray(ResultSet resultSet) throws SQLException, JSONException {
		JSONArray applicantsArray = new JSONArray();
		long currentOpeningId = -1; // Initialize with a value not possible in your context
		while (resultSet.next()) {
			System.out.println("getApplicantsArray");
			long openingId = resultSet.getLong("Opening_Id");
			if (currentOpeningId == -1 || openingId != currentOpeningId) {
				applicantsArray = new JSONArray(); // Moved inside the if block
				currentOpeningId = openingId;
			}
			long applicantId = resultSet.getLong("Applicant_Id");
			// Fetch applicant details correctly from the ResultSet
			JSONObject applicantJSON = new JSONObject();
			applicantJSON.put("applicantGender", resultSet.getString("Applicant_Gender"));
			applicantJSON.put("applicantName", resultSet.getString("Applicant_Name"));
			applicantJSON.put("applicantQualification", resultSet.getString("Applicant_Qualification"));
			applicantJSON.put("applicantId", applicantId);
			applicantJSON.put("applicantEmail", resultSet.getString("Applicant_Email"));
			
			applicantsArray.put(applicantJSON);
		}
		System.out.println(applicantsArray.toString());
		return applicantsArray;
	}

//	private JSONArray getApplicantsDetailsByOpeningId(long openingId) throws SQLException, JSONException {
//		String applicantsQuery = "SELECT A.Application_Id, A.Date, JS.Name AS Job_Seeker_Name, JS.Email AS Job_Seeker_Email, JS.DOB AS Job_Seeker_DOB, JS.Gender AS Job_Seeker_Gender, JS.Experience AS Job_Seeker_Experience, JS.Department_Id AS Job_Seeker_Department_Id, JS.Phone AS Job_Seeker_Phone, JS.Qualification AS Job_Seeker_Qualification FROM Application A JOIN Job_Seeker JS ON A.Job_Seeker_Id = JS.Job_Seeker_Id WHERE A.Opening_Id = ?;";
//		JSONArray applicantsArray = new JSONArray();
//		try (Connection connection = ConnectionClass.CreateCon().getConnection();
//				PreparedStatement preparedStatement = connection.prepareStatement(applicantsQuery)) {
//			preparedStatement.setLong(1, openingId);
//			try (ResultSet resultSet = preparedStatement.executeQuery()) {
//				while (resultSet.next()) {
//					applicantsArray.put(mapApplicantsResultSetToJSONArray(resultSet));
//				}
//			}
//		}
//		return applicantsArray;
//	}
	private JSONArray getApplicantsDetailsByOpeningId(long openingId) throws SQLException, JSONException {
	    String applicantsQuery = "SELECT A.Job_Seeker_Id,A.Application_Id, A.Date, JS.Name AS Job_Seeker_Name, JS.Email AS Job_Seeker_Email, JS.DOB AS Job_Seeker_DOB, JS.Gender AS Job_Seeker_Gender, JS.Experience AS Job_Seeker_Experience, JS.Department_Id AS Job_Seeker_Department_Id, JS.Phone AS Job_Seeker_Phone, JS.Qualification AS Job_Seeker_Qualification FROM Application A JOIN Job_Seeker JS ON A.Job_Seeker_Id = JS.Job_Seeker_Id WHERE A.Opening_Id = ?;";
	    JSONArray applicantsArray = new JSONArray();
	    try (Connection connection = ConnectionClass.CreateCon().getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(applicantsQuery)) {
	        preparedStatement.setLong(1, openingId);
	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            while (resultSet.next()) {
	                JSONObject applicantObject = mapApplicantsResultSetToJSONArraywithSocial(resultSet);
	                JSONArray socialMediaArray = getSocialMediaDetails(resultSet.getLong("Application_Id"));
	                applicantObject.put("SocialMedia", socialMediaArray);
	                applicantsArray.put(applicantObject);
	            }
	        }
	    }
	    return applicantsArray;
	}

	private JSONArray getSocialMediaDetails(long jobSeekerId) throws SQLException, JSONException {
		String socialMediaQuery = "SELECT Link, Platform FROM SocialMedia WHERE Job_Seeker_Id = ?;";
		JSONArray socialMediaArray = new JSONArray();
		try (Connection connection = ConnectionClass.CreateCon().getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(socialMediaQuery)) {
			preparedStatement.setLong(1, jobSeekerId);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					JSONObject socialMediaObject = new JSONObject();
					socialMediaObject.put("Link", resultSet.getString("Link"));
					socialMediaObject.put("Platform", resultSet.getString("Platform"));
					socialMediaArray.put(socialMediaObject);
				}
			}
		}
		return socialMediaArray;
	}

	private JSONArray getApplicantsDetailsByTestId(long Test_Id, boolean isFaceToFace){
		String applicantsQuery = "SELECT A.Application_Id, A.Date, JS.Job_Seeker_Id AS Job_Seeker_Id,JS.Name AS Job_Seeker_Name, JS.Email AS Job_Seeker_Email, JS.DOB AS Job_Seeker_DOB, JS.Gender AS Job_Seeker_Gender, JS.Experience AS Job_Seeker_Experience, JS.Department_Id AS Job_Seeker_Department_Id, JS.Phone AS Job_Seeker_Phone, JS.Qualification AS Job_Seeker_Qualification, R.Status, R.Points FROM Application A JOIN Job_Seeker JS ON A.Job_Seeker_Id = JS.Job_Seeker_Id JOIN Result R ON A.Job_Seeker_Id = R.Job_Seeker_Id WHERE R.Test_Id = ?;";

		JSONArray applicantsArray = new JSONArray();
		try (Connection connection = ConnectionClass.CreateCon().getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(applicantsQuery)) {
			preparedStatement.setLong(1, Test_Id);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					applicantsArray.put(mapApplicantsResultSetToJSONArrayForPanelist(resultSet, isFaceToFace));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return applicantsArray;

//	    JSONArray applicantsArray = new JSONArray();
//	    try (Connection connection = ConnectionClass.CreateCon().getConnection();
//	         PreparedStatement preparedStatement = connection.prepareStatement(applicantsQuery)) {
//	        preparedStatement.setLong(1, openingId);
//	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
//	            while (resultSet.next()) {
//	                JSONObject applicantObject = mapApplicantsResultSetToJSONArraywithSocial(resultSet);
//	                JSONArray socialMediaArray = getSocialMediaDetails(resultSet.getLong("Application_Id"));
//	                applicantObject.put("SocialMedia", socialMediaArray);
//	                applicantsArray.put(applicantObject);
//	            }
//	        }
//	    }
//	    return applicantsArray;
	}

	public JSONArray getApplicantsDetailsByOpeningId(int openingId) {
		String applicantsQuery = "SELECT a.Application_Id, js.Name AS Job_Seeker_Name, js.Email AS Job_Seeker_Email, \n"
				+ "       js.DOB AS Job_Seeker_DOB, js.Gender AS Job_Seeker_Gender, js.Experience AS Job_Seeker_Experience, \n"
				+ "       js.Department_Id AS Job_Seeker_Department_Id, js.Phone AS Job_Seeker_Phone, \n"
				+ "       js.Qualification AS Job_Seeker_Qualification, a.Date AS Application_Date \n"
				+ "FROM Application a \n" + "JOIN Job_Seeker js ON a.Job_Seeker_Id = js.Job_Seeker_Id \n"
				+ "WHERE a.Opening_Id = ?\n";
		try (Connection connection = ConnectionClass.CreateCon().getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(applicantsQuery)) {
			preparedStatement.setInt(1, openingId);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				return mapApplicantsResultSetToJSONArray(resultSet);
			}
		} catch (SQLException | JSONException e) {
			e.printStackTrace();
		}
		return new JSONArray();
	}
	private JSONObject mapApplicantsResultSetToJSONArraywithSocial(ResultSet resultSet) throws SQLException, JSONException {
	    JSONObject applicantObject = new JSONObject();

		int Applicant_Id = Integer.parseInt(resultSet.getString("Job_Seeker_Id"));
		applicantObject.put("Applicant_Id", Applicant_Id);
//	    applicantObject.put("applicationId", resultSet.getLong("Application_Id"));
	    applicantObject.put("date", resultSet.getDate("Date"));
	    applicantObject.put("jobSeekerName", resultSet.getString("Job_Seeker_Name"));
	    applicantObject.put("jobSeekerEmail", resultSet.getString("Job_Seeker_Email"));
	    applicantObject.put("jobSeekerDOB", resultSet.getDate("Job_Seeker_DOB"));
	    applicantObject.put("jobSeekerGender", resultSet.getString("Job_Seeker_Gender"));
	    applicantObject.put("jobSeekerExperience", resultSet.getInt("Job_Seeker_Experience"));
		applicantObject.put("jobSeekerDepartmentId", resultSet.getLong("Job_Seeker_Department_Id"));
		applicantObject.put("jobSeekerPhone", resultSet.getString("Job_Seeker_Phone"));
		applicantObject.put("jobSeekerQualification", resultSet.getString("Job_Seeker_Qualification"));
	    return applicantObject;
	}

	private JSONArray mapApplicantsResultSetToJSONArray(ResultSet resultSet) throws SQLException, JSONException {
		JSONArray applicantsArray = new JSONArray();
		while (resultSet.next()) {
			JSONObject applicantJSON = new JSONObject();
			applicantJSON.put("applicationId", resultSet.getLong("Application_Id"));
			applicantJSON.put("date", resultSet.getDate("Date"));
			applicantJSON.put("jobSeekerName", resultSet.getString("Job_Seeker_Name"));
			applicantJSON.put("jobSeekerEmail", resultSet.getString("Job_Seeker_Email"));
			applicantJSON.put("jobSeekerDOB", resultSet.getDate("Job_Seeker_DOB"));
			applicantJSON.put("jobSeekerGender", resultSet.getString("Job_Seeker_Gender"));
			applicantJSON.put("jobSeekerExperience", resultSet.getInt("Job_Seeker_Experience"));
			applicantJSON.put("jobSeekerDepartmentId", resultSet.getLong("Job_Seeker_Department_Id"));
			applicantJSON.put("jobSeekerPhone", resultSet.getString("Job_Seeker_Phone"));
			applicantJSON.put("jobSeekerQualification", resultSet.getString("Job_Seeker_Qualification"));
//			applicantJSON.put("jobSeekersocialMedia", new ArrayList<>());
			applicantsArray.put(applicantJSON);
		}
		return applicantsArray;
	}

	private JSONArray mapApplicantsResultSetToJSONArrayForPanelist(ResultSet resultSet, boolean isFaceToFace)
			throws SQLException, JSONException {
		JSONArray applicantsArray = new JSONArray();
		while (resultSet.next()) {
			JSONObject applicantJSON = new JSONObject();
			resultSet.getString("Application_Id");
			resultSet.getString("Date");
			int Applicant_Id = Integer.parseInt(resultSet.getString("Job_Seeker_Id"));
			applicantJSON.put("Applicant_Id", Applicant_Id);
			applicantJSON.put("Name", resultSet.getString("Job_Seeker_Name"));
			applicantJSON.put("Email", resultSet.getString("Job_Seeker_Email"));
			applicantJSON.put("DOB", resultSet.getDate("Job_Seeker_DOB"));
			applicantJSON.put("Gender", resultSet.getString("Job_Seeker_Gender"));
			applicantJSON.put("Experience", resultSet.getInt("Job_Seeker_Experience"));
			applicantJSON.put("Department_Id", resultSet.getLong("Job_Seeker_Department_Id"));
			applicantJSON.put("Phone", resultSet.getString("Job_Seeker_Phone"));
			applicantJSON.put("Qualification", resultSet.getString("Job_Seeker_Qualification"));
			applicantJSON.put("Status", resultSet.getString("Status"));
			applicantJSON.put("Points", resultSet.getString("Points"));
			JSONArray applicantsReviewArray = new JSONArray();
			if (isFaceToFace) {
				String FaceToFace = "SELECT NP.Panelist_Id AS Panelist_Id, NP.Job_Seeker_Id AS Job_Seeker_Id, NP.Review AS Review, NP.Points AS Points,\n"
						+ "       P.Name AS Panelist_Name, P.Email AS Panelist_Email, P.Gender AS Panelist_Gender,\n"
						+ "       P.Position AS Panelist_Position\n" + "FROM NotesFromPanelist NP\n"
						+ "JOIN Panelist P ON NP.Panelist_Id = P.Panelist_Id\n" + "WHERE NP.Job_Seeker_Id = ?;\n";

				try (Connection connection = ConnectionClass.CreateCon().getConnection();
						PreparedStatement preparedStatement = connection.prepareStatement(FaceToFace)) {
					preparedStatement.setInt(1, Applicant_Id);
					try (ResultSet resultSetFromFaceToFace = preparedStatement.executeQuery()) {
						while (resultSetFromFaceToFace.next()) {
							applicantsReviewArray.put(mapApplicantsReviewResultSetToJSONArray(resultSetFromFaceToFace));
						}
					}
				} catch (SQLException | JSONException e) {
					e.printStackTrace();
				}
				applicantJSON.put("Review", applicantsReviewArray);
			}
	        JSONArray socialMediaArray = getSocialMediaDetails(Applicant_Id);
	        applicantJSON.put("SocialMedia", socialMediaArray);

//	        applicantsArray.put(applicantJSON);

			applicantsArray.put(applicantJSON);
		}
		return applicantsArray;
	}

	private JSONArray mapApplicantsReviewResultSetToJSONArray(ResultSet resultSet) throws SQLException, JSONException {
		JSONArray applicantsArray = new JSONArray();
		while (resultSet.next()) {
			JSONObject applicantJSON = new JSONObject();
			applicantJSON.put("applicationId", resultSet.getLong("Panelist_Id"));
//			applicantJSON.put("date", resultSet.getDate("Date"));
			applicantJSON.put("jobSeekerName", resultSet.getString("Review"));
			applicantJSON.put("jobSeekerEmail", resultSet.getLong("Points"));
			applicantJSON.put("jobSeekerDOB", resultSet.getString("Panelist_Name"));
			applicantJSON.put("jobSeekerGender", resultSet.getString("Panelist_Email"));
			applicantJSON.put("jobSeekerExperience", resultSet.getString("Panelist_Gender"));
			applicantJSON.put("jobSeekerDepartmentId", resultSet.getString("Panelist_Position"));
//			applicantJSON.put("jobSeekerPhone", resultSet.getString("Job_Seeker_Phone"));
//			applicantJSON.put("jobSeekerQualification", resultSet.getString("Job_Seeker_Qualification"));
			applicantsArray.put(applicantJSON);
		}
		return applicantsArray;
	}

	public JSONArray convertToJSONArray(List<JSONObject> openingDetailsList) {
		return new JSONArray(openingDetailsList);
	}
}
