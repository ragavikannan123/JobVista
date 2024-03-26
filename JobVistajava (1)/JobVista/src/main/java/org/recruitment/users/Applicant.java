package org.recruitment.users;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.util.ConnectionClass;
import org.util.Gender;

public class Applicant {
	private String name;
	private String email;
	private Date Dob;
	private Gender gender;
	private int experience;
	private int departmentId;
	private String phoneNo;
	private String qualification;
	private String photo;
	private List<SocialMedia> sources;
	private List<Skill> skills;
	
	
	public String getName() {
		return name;
	}
	public String getEmail() {
		return email;
	}

	
	

	public Date getDob() {
		return Dob;
	}
	public void setDob(Date dob) {
		Dob = dob;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	public int getExperience() {
		return experience;
	}
	public void setExperience(int experience) {
		this.experience = experience;
	}
	public int getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getQualification() {
		return qualification;
	}
	public void setQualification(String qualification) {
		this.qualification = qualification;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	public Applicant(String name, String email, Date dob, Gender gender, int experience,
			int departmentId, String phoneNo, String qualification, String photo) {
		this.name = name;
		this.email = email;
		Dob = dob;
		this.gender = gender;
		this.experience = experience;
		this.departmentId = departmentId;
		this.phoneNo = phoneNo;
		this.qualification = qualification;
		this.photo = photo;
		this.sources = new ArrayList<SocialMedia>();
		this.skills = new ArrayList<Skill>();
	}
	
	public Applicant(String name,String email) {
		this.name = name;
		this.email = email;
	}
	
	boolean add(String name, String email, Date dob, Gender gender, int experience, int departmentId, String phoneNo, String qualification, String photo) {
		int rowsAffected = 0;
		String query = "Insert into Job_Seeker (Name, Email, DOB, Gender, Experience, Department_Id, Phone, Qualification, Photo) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try(PreparedStatement preparedstatement = ConnectionClass.CreateCon().getConnection().prepareStatement(query)){
			preparedstatement.setString(1, name);
			preparedstatement.setString(2, email);
			preparedstatement.setDate(3, new java.sql.Date(this.Dob.getTime()));
			preparedstatement.setString(4, gender.getValue());
			preparedstatement.setInt(5, experience);
			preparedstatement.setInt(6, departmentId);
			preparedstatement.setString(7, phoneNo);
			preparedstatement.setString(8, qualification);
			preparedstatement.setString(9, photo);
			
			rowsAffected = preparedstatement.executeUpdate();
	    } catch (SQLException e) {
	        System.out.println("Failed to add Jobseeker: " + e.getMessage());
	    }
		return rowsAffected > 0;
	}
	
	
	boolean remove(String name, String email) {
		int rowsAffected = 0;
		String query = "DELETE FROM Job_Seeker where Name like ? And Email like ?";
		try(PreparedStatement preparedstatement = ConnectionClass.CreateCon().getConnection().prepareStatement(query)){
			preparedstatement.setString(1, name);
			preparedstatement.setString(2, email);
			
			rowsAffected = preparedstatement.executeUpdate();
	    } catch (SQLException e) {
	        System.out.println("Failed to delete Jobseeker: " + e.getMessage());
	    }
		return rowsAffected > 0;
	}
	
	boolean applyForJob(String name, String email, int openingId) {
		int rowsAffected = 0;
		String query = "SELECT Job_Seeker_Id FROM Job_Seeker WHERE Name LIKE ? AND Email LIKE ?";

		try(PreparedStatement preparedstatement = ConnectionClass.CreateCon().getConnection().prepareStatement(query)){
			preparedstatement.setString(1, "%" + name + "%");
			preparedstatement.setString(2, "%" + email + "%");

			ResultSet rs = preparedstatement.executeQuery();
			
			while(rs.next()) {
				int jobSeekerId = rs.getInt(1);
				String applyQuery = "Insert into Application (Job_Seeker_Id, Opening_Id, Date) values (?, ?, ?)";
				try(PreparedStatement preparedStatement = ConnectionClass.CreateCon().getConnection().prepareStatement(applyQuery)){
					preparedStatement.setInt(1, jobSeekerId);
					preparedStatement.setInt(2, openingId);
					preparedStatement.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));
					
					rowsAffected = preparedStatement.executeUpdate();
					
				}catch(SQLException e) {
					System.out.println("Failed to apply Jobseeker: " + e.getMessage());
				}
			}	
	    } catch (SQLException e) {
	        System.out.println("Failed to get JobseekerId: " + e.getMessage());
	    }
		return rowsAffected > 0;
	}
	
//	void seeOpenings() {
//		
//	}
//	
//	boolean seeResult(String name) {
//		return false;
//		
//	}
}
