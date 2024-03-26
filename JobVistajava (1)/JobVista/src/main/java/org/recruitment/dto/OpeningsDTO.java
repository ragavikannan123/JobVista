package org.recruitment.dto;

import java.util.Date;

public class OpeningsDTO {
	private long openingId;
	private long departmentId;
	private String description;
	private int experience;
	private String qualification;
	private String departments;
	private String employmentType;
	private String salaryRange;
	private long panelistId;
	private Date startDate;
	private Date endDate;


	public OpeningsDTO(long openingId, String description, int experience, String qualification,
			String departments, String employmentType, String salaryRange, Date startDate,
			Date endDate) {
		this.openingId = openingId;
		this.departmentId = departmentId;
		this.description = description;
		this.experience = experience;
		this.qualification = qualification;
		this.departments = departments;
		this.employmentType = employmentType;
		this.salaryRange = salaryRange;
		this.panelistId = panelistId;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	// Getters and setters

	public long getOpeningId() {
		return openingId;
	}

	public void setOpeningId(long openingId) {
		this.openingId = openingId;
	}

	public long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(long departmentId) {
		this.departmentId = departmentId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public String getDepartments() {
		return departments;
	}

	public void setDepartments(String departments) {
		this.departments = departments;
	}

	public String getEmploymentType() {
		return employmentType;
	}

	public void setEmploymentType(String employmentType) {
		this.employmentType = employmentType;
	}

	public String getSalaryRange() {
		return salaryRange;
	}

	public void setSalaryRange(String salaryRange) {
		this.salaryRange = salaryRange;
	}

	public long getPanelistId() {
		return panelistId;
	}

	public void setPanelistId(long panelistId) {
		this.panelistId = panelistId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
