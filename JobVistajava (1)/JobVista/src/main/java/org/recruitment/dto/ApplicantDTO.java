package org.recruitment.dto;

import java.sql.Date;
import java.util.List;
import org.util.Gender;

public class ApplicantDTO {
    private String name;
    private String email;
    private Date dob;
    private Gender gender;
    private int experience;
    private int departmentId;
    private String phoneNo;
    private String qualification;
    private List<SocialMediaDTO> sources;
    private List<SkillDTO> skills;


    public ApplicantDTO(String name, String email, Date dob, Gender gender, int experience,
                        int departmentId, String phoneNo, String qualification, List<SocialMediaDTO> sources, List<SkillDTO> skills) {
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.gender = gender;
        this.experience = experience;
        this.departmentId = departmentId;
        this.phoneNo = phoneNo;
        this.qualification = qualification;
        this.sources = sources;
        this.skills = skills;
    }

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public Date getDob() {
		return dob;
	}

	public Gender getGender() {
		return gender;
	}

	public int getExperience() {
		return experience;
	}

	public int getDepartmentId() {
		return departmentId;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public String getQualification() {
		return qualification;
	}

//	public String getPhoto() {
//		return photo;
//	}

	public List<SocialMediaDTO> getSources() {
		return sources;
	}

	public List<SkillDTO> getSkills() {
		return skills;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

//	public void setPhoto(String photo) {
//		this.photo = photo;
//	}

	public void setSources(List<SocialMediaDTO> sources) {
		this.sources = sources;
	}

	public void setSkills(List<SkillDTO> skills) {
		this.skills = skills;
	}

}
