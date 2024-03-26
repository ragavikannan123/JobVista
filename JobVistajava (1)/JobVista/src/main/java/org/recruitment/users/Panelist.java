package org.recruitment.users;

import org.util.Gender;

public class Panelist {
	private String name;
	private String email;
	private Gender gender;
	private String position;
	private String organistion;
	private String department;
	
	
	public Panelist(String name, String email, Gender gender, String position) {
		this.name = name;
		this.email = email;
		this.gender = gender;
		this.position = position;
	}
	
	public Panelist(String name, String email) {
		this.name = name;
		this.email = email;
	}
	
	
	
	public Panelist(String name, String email, Gender gender, String position, String organistion, String department) {
		
		this.name = name;
		this.email = email;
		this.gender = gender;
		this.position = position;
		this.organistion = organistion;
		this.department = department;
	}
	
	



//	void addReview(JobSeeker applicant,Test test) {
//		
//	}
	
//	void assingPanlistToOpenings(Panelist panalist, Openings opning){
//		
//	}
	
//	Opening createOpening() {
//		
//	}
	
	
//	Date scheduleInterview(){
//		
//	}
	
	
	
	public Gender getGender() {
		return gender;
	}


	public String getOrganistion() {
		return organistion;
	}



	public String getDepartment() {
		return department;
	}



	public String getPosition() {
		return position;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}


}
